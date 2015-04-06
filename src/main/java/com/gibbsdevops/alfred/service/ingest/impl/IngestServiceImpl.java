package com.gibbsdevops.alfred.service.ingest.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gibbsdevops.alfred.model.events.github.PushEvent;
import com.gibbsdevops.alfred.model.job.Job;
import com.gibbsdevops.alfred.service.build.BuildService;
import com.gibbsdevops.alfred.service.ingest.IngestService;
import com.gibbsdevops.alfred.service.job.JobService;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class IngestServiceImpl implements IngestService {

    private static final Logger LOG = LoggerFactory.getLogger(IngestServiceImpl.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private JobService jobService;

    @Autowired
    private BuildService buildService;

    @Override
    public void handle(PushEvent event) {
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
