package com.gibbsdevops.alfred.service.build.worker;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gibbsdevops.alfred.model.alfred.AlfredJobNode;
import com.gibbsdevops.alfred.model.alfred.utils.AlfredObjectMapperFactory;
import com.gibbsdevops.alfred.service.build.BuildStatusService;
import com.gibbsdevops.alfred.service.build.impl.JobStatusUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.Queue;

public class JmsBuildStatusSender implements BuildStatusService {

    private static final Logger LOG = LoggerFactory.getLogger(JmsBuildStatusSender.class);
    private static final ObjectMapper OBJECT_MAPPER = AlfredObjectMapperFactory.get();

    @Autowired
    @Qualifier("jobStatusQueue")
    private Queue jobStatusQueue;

    @Autowired
    @Qualifier("jobLogQueue")
    private Queue jobLogQueue;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Override
    public void queued(AlfredJobNode job) {
        throw new UnsupportedOperationException();
    }

    String message(Long id, String status) {
        return message(id, status, null, null);
    }

    String message(Long id, String status, String description) {
        return message(id, status, description, null);
    }

    String message(Long id, String status, Integer duration) {
        return message(id, status, null, duration);
    }

    String message(Long id, String status, String description, Integer duration) {
        try {
            return OBJECT_MAPPER.writeValueAsString(new JobStatusUpdate(id, status, description, duration));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to marshal job status", e);
        }
    }

    @Override
    public void starting(AlfredJobNode job) {
        jmsTemplate.convertAndSend(jobStatusQueue, message(job.getId(), "in-progress"));
    }

    @Override
    public void succeeded(AlfredJobNode job, int duration) {
        jmsTemplate.convertAndSend(jobStatusQueue, message(job.getId(), "success", duration));
    }

    @Override
    public void failed(AlfredJobNode job, int duration) {
        jmsTemplate.convertAndSend(jobStatusQueue, message(job.getId(), "failure", duration));
    }

    @Override
    public void errored(AlfredJobNode job, String error) {
        jmsTemplate.convertAndSend(jobStatusQueue, message(job.getId(), "errored", error));
    }

    @Override
    public void logOutput(AlfredJobNode job, int index, String line) {
        LOG.info("not sending logs yet");
    }

}
