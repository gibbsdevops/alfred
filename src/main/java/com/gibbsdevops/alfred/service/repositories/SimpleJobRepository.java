package com.gibbsdevops.alfred.service.repositories;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.gibbsdevops.alfred.model.events.github.PushEvent;
import com.gibbsdevops.alfred.model.job.Job;
import com.gibbsdevops.alfred.service.JobRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@Repository
public class SimpleJobRepository implements JobRepository {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleJobRepository.class);
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String DB_FILE = "jobs-db.json";
    private static int nextId = 1;

    private List<Job> jobs;

    public SimpleJobRepository() {
        jobs = getList();
        LOG.info("Loaded {} jobs from {}", jobs.size(), DB_FILE);
    }

    @Autowired
    private SimpMessagingTemplate template;

    void send(Job job) {
        LOG.info("Sending Job to /topic/jobs: {}", job.getId());
        template.convertAndSend("/topic/jobs", job);
    }

    @Override
    public Job create(PushEvent event) {
        Job job = new Job();
        job.setOrganization(event.getOrganization());
        job.setRepository(event.getRepository());
        job.setRef(event.getRef());
        job.setCommit(event.getHeadCommit());
        job.setPusher(event.getPusher());
        job.setStatus("queued");

        job.setId(jobs.size() + 1);
        jobs.add(job);
        saveList(jobs);

        send(job);
        return job;
    }

    @Override
    public void save(Job job) {

        jobs.set(job.getId() - 1, job);
        saveList(jobs);

        send(job);
    }

    @Override
    public Set<Job> getJobs() {
        return Sets.newHashSet(getList());
    }

    void saveList(List<Job> jobs) {
        File jsonDb = new File(DB_FILE);
        try {
            mapper.writeValue(jsonDb, jobs);
        } catch (IOException e) {
            LOG.warn("Failed to write to {}", DB_FILE, e);
        }
    }

    List<Job> getList() {
        File db = new File(DB_FILE);
        if (!db.exists()) {
            return Lists.newArrayList();
        }

        try {
            return mapper.readValue(db, List.class);
        } catch (IOException e) {
            LOG.warn("Failed to read {}", DB_FILE, e);
            return Lists.newArrayList();
        }
    }

}
