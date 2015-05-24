package com.gibbsdevops.alfred.service.build.impl;

import com.gibbsdevops.alfred.model.alfred.AlfredJobNode;
import com.gibbsdevops.alfred.service.build.BuildQueueSubmitter;
import com.gibbsdevops.alfred.service.build.BuildService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LocalBuildQueueSubmitter implements BuildQueueSubmitter {

    private static final Logger LOG = LoggerFactory.getLogger(BuildServiceImpl.class);

    @Autowired
    private BuildService buildService;

    private ExecutorService executorService;

    @PostConstruct
    public void setup() {
        int alfredWorkerCount = Integer.parseInt(System.getenv().getOrDefault("ALFRED_WORKER_COUNT", "2"));
        executorService = Executors.newFixedThreadPool(alfredWorkerCount);
    }

    @Override
    public void submit(AlfredJobNode job) {
        LOG.info("Submitted job {}", job);
        executorService.execute(new BuildRunnable(job, buildService));
    }

}
