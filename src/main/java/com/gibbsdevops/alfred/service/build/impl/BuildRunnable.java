package com.gibbsdevops.alfred.service.build.impl;

import com.gibbsdevops.alfred.model.job.Job;
import com.gibbsdevops.alfred.service.build.BuildService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

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

        String command = "ci-script.sh";
        if (SystemUtils.IS_OS_WINDOWS) {
            command = "ci-script.bat";
        }

        File workspace = null;
        try {
            workspace = new File("workspace", job.getId().toString());
            FileUtils.forceDelete(workspace);
            workspace.mkdirs();

            String fullCommand = new File(command).getAbsolutePath();

            ProcessBuilder pb = new ProcessBuilder(fullCommand);
            pb.directory(workspace);
            pb.environment().put("ALFRED_JOB_ID", job.getId().toString());
            pb.environment().put("ALFRED_REPO_NAME", job.getRepository().getName());
            pb.environment().put("ALFRED_ORG_NAME", job.getOrganization().getLogin());
            pb.environment().put("ALFRED_GIT_URL", job.getRepository().getGitUrl());
            pb.environment().put("ALFRED_GIT_SSH_URL", job.getRepository().getSshUrl());
            pb.redirectErrorStream(true);

            Process proc = pb.start();
            BuildInputStream stdStream = new BuildInputStream(job, proc.getInputStream(), buildService);

            stdStream.start();

            // TODO new process stuff

            int exitVal = proc.waitFor();

            buildService.finished(job);

        } catch (Throwable t) {
            LOG.warn("Build failed", t);
            buildService.failed(job, t.getMessage());
        } finally {
            Thread.currentThread().setName("Builder - Open");
            if (workspace != null) {
                try {
                    FileUtils.forceDelete(workspace);
                } catch (IOException e) {
                    LOG.warn("Unable to delete temp dir {}", workspace.getAbsolutePath(), e);
                }
            }
        }
    }

}
