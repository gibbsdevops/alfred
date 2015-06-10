package com.gibbsdevops.alfred.service.build.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gibbsdevops.alfred.config.common.JmsConfig;
import com.gibbsdevops.alfred.dao.AlfredNodeDao;
import com.gibbsdevops.alfred.model.alfred.AlfredJobNode;
import com.gibbsdevops.alfred.model.alfred.utils.AlfredObjectMapperFactory;
import com.gibbsdevops.alfred.service.build.BuildStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;

import java.io.IOException;

public class JsmBuildStatusListener {

    private static final Logger LOG = LoggerFactory.getLogger(JsmBuildStatusListener.class);
    private static final ObjectMapper OBJECT_MAPPER = AlfredObjectMapperFactory.get();

    @Autowired
    private BuildStatusService buildStatusService;

    @Autowired
    private AlfredNodeDao alfredNodeDao;


    @JmsListener(destination = JmsConfig.JOB_STATUS_QUEUE_NAME, containerFactory = "defaultJmsContainerFactory")
    public void receive(String body) {
        LOG.info(body);

        JobStatusUpdate update = null;
        try {
            update = OBJECT_MAPPER.readValue(body, JobStatusUpdate.class);
        } catch (IOException e) {
            throw new RuntimeException("Unable to parse JobStatusUpdate", e);
        }

        AlfredJobNode job = alfredNodeDao.getJob(update.getId());

        switch (update.getStatus()) {
            case "in-progress":
                buildStatusService.starting(job);
                break;
            case "succeeded":
                buildStatusService.succeeded(job, update.getDuration());
                break;
            case "failure":
                buildStatusService.failed(job, update.getDuration());
                break;
            case "errored":
                buildStatusService.errored(job, update.getDescription());
                break;
            default:
                throw new RuntimeException("Unexpected job status: " + update.getStatus());
        }
    }

}
