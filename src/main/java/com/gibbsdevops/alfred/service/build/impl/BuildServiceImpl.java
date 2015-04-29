package com.gibbsdevops.alfred.service.build.impl;

import com.gibbsdevops.alfred.dao.AlfredJobDao;
import com.gibbsdevops.alfred.dao.AlfredJobLineDao;
import com.gibbsdevops.alfred.model.alfred.*;
import com.gibbsdevops.alfred.repository.AlfredRepository;
import com.gibbsdevops.alfred.service.build.BuildService;
import com.gibbsdevops.alfred.service.job.repositories.JobOutputRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

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
    private AlfredJobDao alfredJobDao;

    @Autowired
    private AlfredJobLineDao alfredJobLineDao;

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
        updateAndSend(job.getId(), j -> {
            j.setStatus("in-progress");
        });
    }

    @Override
    public void succeeded(AlfredJobNode job) {
        LOG.info("Building job {} succeeded", job);
        updateAndSend(job.getId(), j -> {
            j.setStatus("complete");
        });
    }

    @Override
    public void failed(AlfredJobNode job) {
        LOG.info("Building job {} completed with failure", job);
        updateAndSend(job.getId(), j -> {
            j.setStatus("failed");
        });
    }

    @Override
    public void errored(AlfredJobNode job, String error) {
        LOG.info("Building job {} errored: {}", job, error);
        updateAndSend(job.getId(), j -> {
            j.setStatus("failed");
            j.setError(error);
        });
    }

    @Override
    public void logOutput(AlfredJobNode job, int index, String line) {
        LOG.info("Build Output {}: {}", job, line);
        JobLine jobLine = new JobLine();
        jobLine.setJobId(job.getId());
        jobLine.setIndex(index);
        jobLine.setLine(line);
        alfredJobLineDao.save(jobLine);
        messagingTemplate.convertAndSend("/topic/job-line", jobLine);
    }

    AlfredJob updateAndSend(Long id, AlfredJobUpdate update) {
        AlfredJob job = alfredJobDao.findOne(id);
        update.exec(job);
        job = alfredRepository.save(job);
        send(job);
        return job;
    }

    void send(AlfredJob node) {
        LOG.info("Sending Job to /topic/jobs: {}", node);
        messagingTemplate.convertAndSend("/topic/jobs", node);
    }

    interface AlfredJobUpdate {
        void exec(AlfredJob j);
    }
}
