package com.gibbsdevops.alfred.service.ingest.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gibbsdevops.alfred.model.alfred.AlfredCommitNode;
import com.gibbsdevops.alfred.model.alfred.AlfredGitUser;
import com.gibbsdevops.alfred.model.alfred.AlfredRepoNode;
import com.gibbsdevops.alfred.model.alfred.AlfredUser;
import com.gibbsdevops.alfred.model.github.GHCommit;
import com.gibbsdevops.alfred.model.github.events.GHPushEvent;
import com.gibbsdevops.alfred.model.job.Job;
import com.gibbsdevops.alfred.repository.AlfredRepository;
import com.gibbsdevops.alfred.service.build.BuildService;
import com.gibbsdevops.alfred.service.ingest.IngestService;
import com.gibbsdevops.alfred.service.job.JobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class IngestServiceImpl implements IngestService {

    private static final Logger LOG = LoggerFactory.getLogger(IngestServiceImpl.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private AlfredRepository alfredRepository;

    @Autowired
    private JobService jobService;

    @Autowired
    private BuildService buildService;

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

        AlfredUser org = null;
        if (event.getOrganization() != null) {
            org = alfredRepository.save(AlfredUser.from(event.getOrganization()));
        }

        AlfredUser sender = alfredRepository.save(AlfredUser.from(event.getSender()));

        AlfredRepoNode repo = AlfredRepoNode.from(event.getRepository());
        repo = alfredRepository.save(repo);

        AlfredGitUser pusher = alfredRepository.save(AlfredGitUser.from(event.getPusher()));

        String orgName = event.getRepository().getOrganization();
        if (orgName != null) {
            if (!orgName.equals(org.getName())) {
                org = alfredRepository.getOrgByName(orgName);
            }
            repo.setOrganization(org);
        } else {
            AlfredUser owner = alfredRepository.getUserByName(event.getRepository().getOwner().getName());
            repo.setOwner(owner);
        }
        
        for (GHCommit c : event.getCommits()) {
            AlfredGitUser author = alfredRepository.save(AlfredGitUser.from(c.getAuthor()));
            AlfredGitUser committer = alfredRepository.save(AlfredGitUser.from(c.getCommitter()));

            AlfredCommitNode commit = AlfredCommitNode.from(c);
            commit.setRepo(repo);
            commit.setSender(sender);
            commit.setPusher(pusher);
            commit.setAuthor(author);
            commit.setCommitter(committer);

            Job job = new Job();
            job.setOrganization(event.getOrganization());
            job.setRepository(event.getRepository());
            job.setRef(event.getRef());
            job.setCommit(event.getHeadCommit());
            job.setPusher(event.getPusher());
            job.setStatus("queued");

            // save job
            jobService.save(job);

            // submit job for building
            buildService.submit(job);
        }

    }

}
