package com.gibbsdevops.alfred.service.job.impl;

import com.gibbsdevops.alfred.model.job.Job;
import com.gibbsdevops.alfred.service.job.JobService;
import com.gibbsdevops.alfred.service.job.repositories.JobOutputRepository;
import com.gibbsdevops.alfred.service.job.repositories.JobRepository;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;

public class DefaultJobService implements JobService {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultJobService.class);

    @Autowired
    JobRepository jobRepository;

    @Autowired
    JobOutputRepository jobOutputRepository;

    @Autowired
    private SimpMessagingTemplate template;

    @Override
    public void save(Job job) {
        LOG.info("Saving Job {}", job);
        jobRepository.save(job);
        LOG.info("Sending Job to /topic/jobs: {}", job);
        template.convertAndSend("/topic/jobs", job);
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Job> getJobs() {
        return Lists.newArrayList(jobRepository.getJobs());
    }

    @Override
    public Job getJob(int id) {
        return jobRepository.getJob(id);
    }

}
