package com.gibbsdevops.alfred.service.build.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gibbsdevops.alfred.model.alfred.AlfredJobNode;
import com.gibbsdevops.alfred.model.alfred.utils.AlfredObjectMapperFactory;
import com.gibbsdevops.alfred.service.build.BuildQueueSubmitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class JmsBuildQueueSubmitter implements BuildQueueSubmitter {

    private static final Logger LOG = LoggerFactory.getLogger(JmsBuildQueueSubmitter.class);

    @Autowired
    private JmsTemplate jmsTemplate;

    private final ObjectMapper objectMapper = AlfredObjectMapperFactory.get();

    @Override
    public void submit(AlfredJobNode job) {
        try {
            jmsTemplate.convertAndSend("jobs", objectMapper.writeValueAsString(job));
            LOG.info("Job {} sent to JMS broker", job.getId());

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Unable to serialize job", e);
        }
    }

}
