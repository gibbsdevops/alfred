package com.gibbsdevops.alfred.service.build.worker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gibbsdevops.alfred.config.common.JmsConfig;
import com.gibbsdevops.alfred.model.alfred.AlfredJobNode;
import com.gibbsdevops.alfred.model.alfred.utils.AlfredObjectMapperFactory;
import com.gibbsdevops.alfred.service.build.BuildStatusService;
import com.gibbsdevops.alfred.service.build.impl.BuildRunnable;
import com.gibbsdevops.alfred.service.build.impl.JmsJobQueueSubmitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;

import java.io.IOException;

public class JmsJobBuildListener {

    private static final Logger LOG = LoggerFactory.getLogger(JmsJobQueueSubmitter.class);

    private static final ObjectMapper OBJECT_MAPPER = AlfredObjectMapperFactory.get();

    @Autowired
    private BuildStatusService buildStatusService;

    @JmsListener(destination = JmsConfig.JOB_BUILD_QUEUE_NAME, containerFactory = "jobExecutorContainerFactory")
    public void receiveJob(String jobBody) {
        LOG.debug("Received {}", jobBody);

        AlfredJobNode job = null;
        try {
            job = OBJECT_MAPPER.readValue(jobBody, AlfredJobNode.class);
        } catch (IOException e) {
            LOG.error("Failed to parse job", e);
            return;
        }

        BuildRunnable buildRunnable = new BuildRunnable(job, buildStatusService);
        buildRunnable.run();
    }

}
