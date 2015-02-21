package com.gibbsdevops.alfred.service.build.impl;

import com.gibbsdevops.alfred.model.job.Job;
import com.gibbsdevops.alfred.service.build.BuildService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class BuildRunnable implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(BuildRunnable.class);

    private final Job job;
    private final BuildService buildService;

    public BuildRunnable(Job job, BuildService buildService) {
        this.job = job;
        this.buildService = buildService;
    }

    void wait(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
        }
    }

    @Override
    public void run() {
        Thread.currentThread().setName("Builder #" + job.getId());
        LOG.info("Starting Builder #{}", job.getId());
        buildService.starting(job);

        File executable = new File("ci-script.sh");

        String[] cmd = new String[]{executable.getAbsolutePath()};
        Runtime rt = Runtime.getRuntime();

        try {
            Process proc = rt.exec(cmd);

            BuildInputStream errStream = new BuildInputStream(job, proc.getErrorStream(), buildService);
            BuildInputStream stdStream = new BuildInputStream(job, proc.getInputStream(), buildService);

            errStream.start();
            stdStream.start();

            int exitVal = proc.waitFor();

            buildService.finished(job);

        } catch (Throwable t) {
            LOG.warn("Build failed", t);
            buildService.failed(job, t.getMessage());
        } finally {
            Thread.currentThread().setName("Builder - Open");
        }
    }

}
