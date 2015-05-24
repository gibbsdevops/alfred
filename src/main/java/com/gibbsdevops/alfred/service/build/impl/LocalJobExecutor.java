package com.gibbsdevops.alfred.service.build.impl;

import com.gibbsdevops.alfred.model.alfred.AlfredJobNode;
import com.gibbsdevops.alfred.service.build.BuildStatusService;
import com.gibbsdevops.alfred.service.build.JobExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class LocalJobExecutor implements JobExecutor {

    private static final Logger LOG = LoggerFactory.getLogger(JmsJobQueueSubmitter.class);

    @Autowired
    private BuildStatusService buildStatusService;

    private ExecutorService executorService;

    @PostConstruct
    public void setup() {
        int alfredWorkerCount = Integer.parseInt(System.getenv().getOrDefault("ALFRED_WORKER_COUNT", "2"));
        executorService = Executors.newFixedThreadPool(alfredWorkerCount);
    }

    @Override
    public void execute(AlfredJobNode job) {
        LOG.info("Sending job to executor pool {}", job);
        executorService.execute(new BuildRunnable(job, buildStatusService));
    }

}
