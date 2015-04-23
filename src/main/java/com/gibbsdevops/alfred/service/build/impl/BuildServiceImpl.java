package com.gibbsdevops.alfred.service.build.impl;

import com.gibbsdevops.alfred.model.alfred.AlfredJob;
import com.gibbsdevops.alfred.model.alfred.AlfredJobNode;
import com.gibbsdevops.alfred.model.alfred.AlfredRepoNode;
import com.gibbsdevops.alfred.model.alfred.AlfredUser;
import com.gibbsdevops.alfred.repository.AlfredRepository;
import com.gibbsdevops.alfred.service.build.BuildService;
import com.gibbsdevops.alfred.service.job.repositories.JobOutputRepository;
import org.kohsuke.github.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private AlfredRepository alfredRepository;

    @Autowired
    private JobOutputRepository jobOutputRepository;

    @Override
    public void submit(AlfredJobNode job) {
        AlfredRepoNode repo = job.getCommit().getRepo();
        AlfredUser owner = repo.getOwner();

        /*
        try {
            GitHub gitHub = GitHub.connect();
            GHRepository ghRepo = null;
            if ("Organization".equals(owner.getType())) {
                GHOrganization ghOrg = gitHub.getOrganization(owner.getLogin());
                ghRepo = ghOrg.getRepositories().get(repo.getName());
            } else if ("User".equals(owner.getType())) {
                GHUser ghUser = gitHub.getUser(owner.getName());
                ghRepo = ghUser.getRepository(repo.getName());
            } else {
                throw new IllegalArgumentException("Unexpected user type: " + owner.getType());
            }

            ghRepo.createCommitStatus(job.getCommit().getHash(), GHCommitState.PENDING, "http://alfred.gibbsdevops.com/#/jobs/" + job.getId(), "Building...");

        } catch (IOException e) {
            LOG.warn("Failed to mark github status as pending", e);
        }
        */

        LOG.info("Submitted job {}", job);
        buildExecutor.execute(new BuildRunnable(job, this));
    }

    @Override
    public void starting(AlfredJobNode job) {
        LOG.info("Started building job {}", job);
        job.setStatus("in-progress");
        saveAndSend(job);
    }

    @Override
    public void finished(AlfredJobNode job) {
        LOG.info("Building job {} complete", job);
        job.setStatus("complete");
        saveAndSend(job);
    }

    @Override
    public void failed(AlfredJobNode job, String reason) {
        LOG.info("Building job {} failed: {}", job, reason);
        job.setStatus("failed");
        job.setError(reason);
        saveAndSend(job);
    }

    @Override
    public void logOutput(AlfredJobNode job, String line) {
        LOG.info("Build Output {}: {}", job, line);
        // jobOutputRepository.append(job.getId(), line);
    }

    AlfredJobNode saveAndSend(AlfredJobNode job) {
        AlfredJob normal = job.normalize();
        normal = alfredRepository.save(normal);

        LOG.info("Sending Job to /topic/jobs: {}", job);
        messagingTemplate.convertAndSend("/topic/jobs", normal);
        return job;
    }

}
