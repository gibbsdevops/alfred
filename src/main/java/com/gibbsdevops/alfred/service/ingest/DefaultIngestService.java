package com.gibbsdevops.alfred.service.ingest;

import com.gibbsdevops.alfred.model.alfred.*;
import com.gibbsdevops.alfred.model.github.GHCommit;
import com.gibbsdevops.alfred.model.github.GHOrganization;
import com.gibbsdevops.alfred.model.github.GHPerson;
import com.gibbsdevops.alfred.model.github.GHRepository;
import com.gibbsdevops.alfred.model.github.events.GHPushEvent;
import com.gibbsdevops.alfred.repository.AlfredRepository;
import com.gibbsdevops.alfred.service.build.BuildQueueSubmitter;
import com.gibbsdevops.alfred.service.build.BuildStatusService;
import com.gibbsdevops.alfred.service.github.GithubApiService;
import com.gibbsdevops.alfred.utils.rest.DateTimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class DefaultIngestService implements IngestService {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultIngestService.class);

    private GithubApiService githubApiService;
    private SimpMessagingTemplate template;
    private AlfredRepository alfredRepository;
    private BuildQueueSubmitter buildQueue;
    private BuildStatusService buildStatusService;

    @Override
    public void handle(GHPushEvent event) {
        LOG.info("New GutHub PushEvent {}", event.getGuid());

        // send event to client
        if (event.getHeadCommit() != null) {
            LOG.info("Sending PushEvent to /topic/github: {}", event.getGuid());
            template.convertAndSend("/topic/github", event);
        }

        // we are done if there are no commits
        if (event.getHeadCommit() == null) {
            return;
        }

        AlfredUser owner = null;
        if (event.getOrganization() != null) {
            GHOrganization ghOrg = githubApiService.getOrganization(event.getOrganization().getUrl());
            owner = alfredRepository.save(AlfredUser.from(ghOrg));
        }

        GHPerson ghSender = githubApiService.getPerson(event.getSender().getUrl());
        AlfredUser sender = alfredRepository.save(AlfredUser.from(ghSender));

        String repoUrl = AlfredRepoProperties.extractUrlFromForksUrl(event.getRepository().getForksUrl());

        GHRepository ghRepo = githubApiService.getRepository(repoUrl);
        AlfredRepoNode repoNode = AlfredRepoNode.from(ghRepo);

        if (owner != null) {
            // owned by organization
            repoNode.setOwner(owner);
        } else {
            // owned by user
            GHPerson ghOwner = githubApiService.getPerson(ghRepo.getOwner().getUrl());
            owner = alfredRepository.save(AlfredUser.from(ghOwner));
            repoNode.setOwner(owner);
        }

        AlfredRepo repo = repoNode.normalize();
        repo = alfredRepository.save(repo);
        repoNode.setId(repo.getId()); // for use when normalizing

        AlfredGitUser pusher = alfredRepository.save(AlfredGitUser.from(event.getPusher()));

        for (GHCommit c : event.getCommits()) {
            AlfredGitUser author = alfredRepository.save(AlfredGitUser.from(c.getAuthor()));
            AlfredGitUser committer = alfredRepository.save(AlfredGitUser.from(c.getCommitter()));

            AlfredCommitNode commitNode = AlfredCommitNode.from(c);
            commitNode.setRepo(repoNode);
            commitNode.setSender(sender);
            commitNode.setPusher(pusher);
            commitNode.setAuthor(author);
            commitNode.setCommitter(committer);
            AlfredCommit commit = alfredRepository.save(commitNode.normalize());
            commitNode = alfredRepository.getCommitNode(commit.getId());

            AlfredJobNode job = new AlfredJobNode();
            job.setCreatedAt(DateTimeUtils.getClock().millis() / 1000);
            job.setCommit(commitNode);
            job.setStatus("queued");

            alfredRepository.save(job.normalize());

            // submit job for building
            try {
                buildQueue.submit(job);
            } catch (Exception e) {
                buildStatusService.errored(job, e.getMessage());
                throw e;
            }
        }

    }

    //<editor-fold desc="Getters and Setters">
    @Autowired
    public void setGithubApiService(GithubApiService githubApiService) {
        this.githubApiService = githubApiService;
    }

    @Autowired
    public void setTemplate(SimpMessagingTemplate template) {
        this.template = template;
    }

    @Autowired
    public void setAlfredRepository(AlfredRepository alfredRepository) {
        this.alfredRepository = alfredRepository;
    }

    @Autowired
    public void setBuildQueue(BuildQueueSubmitter buildQueue) {
        this.buildQueue = buildQueue;
    }

    @Autowired
    public void setBuildStatusService(BuildStatusService buildStatusService) {
        this.buildStatusService = buildStatusService;
    }
    //</editor-fold>

}
