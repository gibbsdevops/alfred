package com.gibbsdevops.alfred.service.build.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.gibbsdevops.alfred.dao.AlfredJobDao;
import com.gibbsdevops.alfred.dao.AlfredJobLineDao;
import com.gibbsdevops.alfred.model.alfred.*;
import com.gibbsdevops.alfred.model.alfred.utils.AlfredObjectMapperFactory;
import com.gibbsdevops.alfred.repository.AlfredRepository;
import com.gibbsdevops.alfred.service.build.BuildService;
import com.gibbsdevops.alfred.utils.rest.DefaultJsonRestClient;
import com.gibbsdevops.alfred.utils.rest.JsonRestClient;
import com.gibbsdevops.alfred.utils.rest.RestRequest;
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
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private AlfredRepository alfredRepository;

    @Autowired
    private AlfredJobDao alfredJobDao;

    @Autowired
    private AlfredJobLineDao alfredJobLineDao;

    private JsonRestClient jsonRestClient = new DefaultJsonRestClient();

    public static class GitHubStatus {

        private String state;
        private String targetUrl;
        private String description;
        private String context;

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getTargetUrl() {
            return targetUrl;
        }

        public void setTargetUrl(String targetUrl) {
            this.targetUrl = targetUrl;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getContext() {
            return context;
        }

        public void setContext(String context) {
            this.context = context;
        }

    }

    public void createGithubStatus(long jobId, String repoUrl, String hash, String statusString, String description) {
        GitHubStatus status = new GitHubStatus();
        status.setState(statusString);
        status.setTargetUrl("http://alfred.gibbsdevops.com/#/jobs/" + jobId);
        status.setDescription(description);
        status.setContext("continuous-integration/alfred");

        RestRequest post = RestRequest.post(repoUrl + "/statuses/" + hash, status);
        post.basicAuthorization(System.getenv("GITHUB_LOGIN"), System.getenv("GITHUB_PASSWORD"));

        JsonNode node = jsonRestClient.exec(post).as(JsonNode.class);
    }

    @Override
    public void queued(AlfredJobNode job) {
        LOG.info("Queued job {}", job);
        updateAndSend(job.getId(), j -> {
            j.setStatus("queued");
        });

        AlfredCommitNode commit = job.getCommit();
        String repoUrl = commit.getRepo().getUrl();
        String hash = commit.getHash();
        createGithubStatus(job.getId(), repoUrl, hash, "pending", "In progress !!");
    }

    @Override
    public void starting(AlfredJobNode job) {
        LOG.info("Started building job {}", job);
        updateAndSend(job.getId(), j -> {
            j.setStatus("in-progress");
        });
    }

    @Override
    public void succeeded(AlfredJobNode job, int duration) {
        LOG.info("Building job {} succeeded", job);
        updateAndSend(job.getId(), j -> {
            j.setDuration(duration);
            j.setStatus("success");
        });

        AlfredCommitNode commit = job.getCommit();
        String repoUrl = commit.getRepo().getUrl();
        String hash = commit.getHash();
        createGithubStatus(job.getId(), repoUrl, hash, "success", "Success !!");
    }

    @Override
    public void failed(AlfredJobNode job, int duration) {
        LOG.info("Building job {} completed with failure", job);
        updateAndSend(job.getId(), j -> {
            j.setDuration(duration);
            j.setStatus("failed");
        });

        AlfredCommitNode commit = job.getCommit();
        String repoUrl = commit.getRepo().getUrl();
        String hash = commit.getHash();
        createGithubStatus(job.getId(), repoUrl, hash, "failure", "Failed !!");
    }

    @Override
    public void errored(AlfredJobNode job, String error) {
        LOG.info("Building job {} errored: {}", job, error);
        updateAndSend(job.getId(), j -> {
            j.setStatus("errored");
            j.setError(error);
        });

        AlfredCommitNode commit = job.getCommit();
        String repoUrl = commit.getRepo().getUrl();
        String hash = commit.getHash();
        createGithubStatus(job.getId(), repoUrl, hash, "error", "Error !!");
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
        try {
            messagingTemplate.convertAndSend("/topic/jobs", AlfredObjectMapperFactory.get().writeValueAsString(node));
        } catch (JsonProcessingException e) {
            LOG.warn("Unable to convert job", e);
        }
    }

    interface AlfredJobUpdate {
        void exec(AlfredJob j);
    }
}
