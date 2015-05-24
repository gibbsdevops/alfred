package com.gibbsdevops.alfred.service.build.impl;

import com.gibbsdevops.alfred.model.alfred.AlfredCommitNode;
import com.gibbsdevops.alfred.model.alfred.AlfredJobNode;
import com.gibbsdevops.alfred.model.alfred.AlfredRepoNode;
import com.gibbsdevops.alfred.service.build.BuildStatusService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SystemUtils;
import org.kohsuke.github.GHCommitState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class BuildRunnable implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(BuildRunnable.class);

    private final AlfredJobNode job;
    private final BuildStatusService buildStatusService;

    public BuildRunnable(AlfredJobNode job, BuildStatusService buildStatusService) {
        this.job = job;
        this.buildStatusService = buildStatusService;
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
        buildStatusService.starting(job);

        String command = "ci-script.sh";
        if (SystemUtils.IS_OS_WINDOWS) {
            command = "ci-script.bat";
        }

        File workspace = null;
        try {
            workspace = new File("workspace", job.getId().toString()).getAbsoluteFile();
            if (workspace.exists()) {
                FileUtils.forceDelete(workspace);
            }
            workspace.mkdirs();

            String fullCommand = new File(command).getAbsolutePath();

            /*
            if (job.getOrganization() == null) {
                throw new RuntimeException("Job has no org");
            }
            */

            AlfredCommitNode commit = job.getCommit();
            if (commit == null) {
                throw new RuntimeException("Job has no commit");
            }

            AlfredRepoNode repo = commit.getRepo();

            ProcessBuilder pb = new ProcessBuilder(fullCommand);
            pb.directory(workspace);
            pb.environment().put("ALFRED_JOB_ID", job.getId().toString());
            pb.environment().put("ALFRED_REPO_NAME", repo.getName());
            // pb.environment().put("ALFRED_ORG_NAME", job.getOrganization().getLogin());
            pb.environment().put("ALFRED_HTML_URL", repo.getHtmlUrl());
            pb.environment().put("ALFRED_SSH_URL", repo.getSshUrl());
            pb.environment().put("ALFRED_GIT_URL", repo.getGitUrl());
            pb.environment().put("ALFRED_CLONE_URL", repo.getCloneUrl());
            pb.environment().put("ALFRED_COMMIT", commit.getHash());
            pb.redirectErrorStream(true);

            long startedAt = System.currentTimeMillis();

            Process proc = pb.start();
            BuildInputStream stdStream = new BuildInputStream(job, proc.getInputStream(), buildStatusService);

            stdStream.start();

            int exitVal = proc.waitFor();
            LOG.info("Exit value of job was {}", exitVal);

            int duration = (int) ((System.currentTimeMillis() - startedAt) / 1000);

            GHCommitState state = GHCommitState.FAILURE;
            if (exitVal == 0) {
                state = GHCommitState.SUCCESS;
                buildStatusService.succeeded(job, duration);
            } else {
                buildStatusService.failed(job, duration);
            }

            /*
            try {
                GitHub gitHub = GitHub.connect();
                GHOrganization ghOrg = gitHub.getOrganization(job.getOrganization().getLogin());
                GHRepository ghRepo = ghOrg.getRepositories().get(job.getRepository().getName());
                ghRepo.createCommitStatus(commit.getId(), state, "http://alfred.gibbsdevops.com/#/jobs/" + job.getId(), "complete");
            } catch (IOException e) {
                LOG.warn("Failed to mark github status as complete", e);
            }
            */

        } catch (Throwable t) {
            LOG.warn("Build errored", t);
            buildStatusService.errored(job, t.getMessage());
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
