package com.gibbsdevops.alfred.service.build.impl;

import com.gibbsdevops.alfred.model.job.Job;
import com.gibbsdevops.alfred.service.build.BuildService;
import com.gibbsdevops.alfred.service.job.repositories.JobOutputRepository;
import com.gibbsdevops.alfred.service.job.JobService;
import org.kohsuke.github.GHCommitState;
import org.kohsuke.github.GHOrganization;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

@Service
public class BuildServiceImpl implements BuildService {

    private static final Logger LOG = LoggerFactory.getLogger(BuildServiceImpl.class);

    @Autowired
    @Qualifier("buildExecutor")
    ExecutorService buildExecutor;

    @Autowired
    private JobService jobService;

    @Autowired
    private JobOutputRepository jobOutputRepository;

    @Override
    public void submit(Job job) {

        try {
            GitHub gitHub = GitHub.connect();
            GHOrganization ghOrg = gitHub.getOrganization(job.getOrganization().getLogin());
            GHRepository ghRepo = ghOrg.getRepositories().get(job.getRepository().getName());
            ghRepo.createCommitStatus(job.getCommit().getId(), GHCommitState.PENDING, "http://alfred.gibbsdevops.com", "Building...");
        } catch (IOException e) {
            LOG.warn("Failed to mark github status as pending", e);
        }

        LOG.info("Submitted job {}", job);
        buildExecutor.execute(new BuildRunnable(job, this));
    }

    @Override
    public void starting(Job job) {
        LOG.info("Started building job {}", job);

        job.setStatus("in-progress");
        jobService.save(job);
    }

    @Override
    public void finished(Job job) {
        LOG.info("Building job {} complete", job);

        job.setStatus("complete");
        jobService.save(job);
    }

    @Override
    public void failed(Job job, String reason) {
        LOG.info("Building job {} failed: {}", job, reason);
        job.setStatus("failed");
        job.setError(reason);
        jobService.save(job);
    }

    @Override
    public void logOutput(Job job, String line) {
        LOG.info("Build Output {}: {}", job, line);
        jobOutputRepository.append(job.getId(), line);
    }

}
