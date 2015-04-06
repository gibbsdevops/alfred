package com.gibbsdevops.alfred.service.job.repositories.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gibbsdevops.alfred.service.job.repositories.JobRepository;
import com.google.common.collect.Sets;
import com.gibbsdevops.alfred.model.job.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public class FileJobRepository implements JobRepository {

    private static final Logger LOG = LoggerFactory.getLogger(FileJobRepository.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    private File path;

    public FileJobRepository(File path) {
        this.path = path;
    }

    @Override
    public void save(Job job) {
        if (job.getId() == null) {
            job.setId(nextId());
            job.setVersion(0);
        } else {
            job.setVersion(job.getVersion() + 1);
        }
        write(job);
    }

    void write(Job job) {
        path.mkdirs();
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
        return new File(path, String.format("%07d", id) + ".json");
    }

    @Override
    public Set<Job> getJobs() {
        Set<Job> jobs = Sets.newHashSet();

        File f;
        for (int id = 0; ; id++) {
            f = fileFor(id);
            if (f.exists()) {
                try {
                    jobs.add(mapper.readValue(f, Job.class));
                } catch (IOException e) {
                    throw new RuntimeException("Unable to load file: " + f.getAbsolutePath(), e);
                }
            } else {
                return jobs;
            }
        }
    }

    @Override
    public Job getJob(int id) {
        File f = fileFor(id);
        try {
            return mapper.readValue(f, Job.class);
        } catch (IOException e) {
            throw new RuntimeException("Unable to load file: " + f.getAbsolutePath(), e);
        }
    }

}
