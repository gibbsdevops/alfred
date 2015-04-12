package com.gibbsdevops.alfred.service.ingest.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gibbsdevops.alfred.model.alfred.*;
import com.gibbsdevops.alfred.model.github.events.GHPushEvent;
import com.gibbsdevops.alfred.model.job.Job;
import com.gibbsdevops.alfred.repository.AlfredUserRepository;
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
    private AlfredUserRepository alfredUserRepository;

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

        AlfredRepoProperties repoProps = AlfredRepoProperties.from(event.getRepository());
        AlfredRepoNode repo = AlfredRepoNode.from(repoProps);

        if (event.getOrganization() != null) {
            AlfredUser org = AlfredUser.from(event.getOrganization());
            alfredUserRepository.save(org);
            repo.setOrganization(org);
        }

        AlfredUser sender = AlfredUser.from(event.getSender());
        alfredUserRepository.save(sender);

        event.getCommits().stream().forEach(c ->  {
            AlfredCommitProperties commitProperties = AlfredCommitProperties.from(c);
            AlfredCommitNode commit = AlfredCommitNode.from(commitProperties);
            commit.setRepo(repo);

            // commit.setSender?
            // author? commiter?

            // TODO: stuff with commit

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
        });

    }

}
