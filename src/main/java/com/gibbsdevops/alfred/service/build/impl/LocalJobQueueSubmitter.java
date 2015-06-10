package com.gibbsdevops.alfred.service.build.impl;

import com.gibbsdevops.alfred.model.alfred.AlfredJobNode;
import com.gibbsdevops.alfred.service.build.BuildQueueSubmitter;
import com.gibbsdevops.alfred.service.build.JobExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

// @Service
public class LocalJobQueueSubmitter implements BuildQueueSubmitter {

    private static final Logger LOG = LoggerFactory.getLogger(LocalJobQueueSubmitter.class);

    @Autowired
    private JobExecutor jobExecutor;

    @Override
    public void submit(AlfredJobNode job) {
        LOG.info("Submitted job {}", job);
        jobExecutor.execute(job);
    }

}
