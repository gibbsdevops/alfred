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
    public void save(Job job) {
        if (job.getId() == null) {
            job.setId(nextId());
        }
        write(job);
        send(job);
    }

    void write(Job job) {
        new File("jobs").mkdirs();
        File f = fileFor(job.getId());
        try {
            mapper.writeValue(f, job);
        } catch (IOException e) {
            throw new RuntimeException("Unable to write file: " + f.getAbsolutePath(), e);
        }
    }

    int nextId() {
        int id = 0;
        while (true) {
            if (!fileFor(id).exists()) {
                return id;
            }
            id++;
        }
    }

    File fileFor(int id) {
        return new File(new File("jobs"), String.format("%07d", id) + ".json");
    }

    @Override
    public Set<Job> getJobs() {
        Set<Job> jobs = Sets.newHashSet();

        File f;
        boolean done = false;
        for (int id = 0; done; id++) {
            f = fileFor(id);
            if (f.exists()) {
                try {
                    jobs.add(mapper.readValue(f, Job.class));
                } catch (IOException e) {
                    throw new RuntimeException("Unable to load file: " + f.getAbsolutePath(), e);
                }
            } else {
                done = true;
            }
        }

        return jobs;
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
