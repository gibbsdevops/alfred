package com.gibbsdevops.alfred.service.repositories;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gibbsdevops.alfred.model.events.job.JobLine;
import com.gibbsdevops.alfred.model.job.Job;
import com.gibbsdevops.alfred.model.job.JobOutput;
import com.gibbsdevops.alfred.service.JobOutputRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class JobOutputRepositoryImpl implements JobOutputRepository {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleJobRepository.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private SimpMessagingTemplate template;

    @Override
    public void append(Job job, String line) {
        JobOutput output = load(job);
        output.getBuildOutput().add(line);
        save(output);

        JobLine jobLine = new JobLine();
        jobLine.setId(job.getId());
        jobLine.setPos(output.getBuildOutput().size() - 1);
        jobLine.setLine(line);

        LOG.info("Sending JobLine to /topic/job-line: {}", job.getId());
        template.convertAndSend("/topic/job-line", jobLine);
    }

    File getFile(int id) {
        new File("jobs").mkdirs();
        return new File(new File("jobs"), String.format("%07d", id) + "-output.json");
    }

    JobOutput load(Job job) {
        File f = getFile(job.getId());

        if (!f.exists()) {
            return create(job);
        }

        try {
            return mapper.readValue(f, JobOutput.class);
        } catch (IOException e) {
            LOG.warn("Failed to read {}", f.getAbsolutePath(), e);
            return create(job);
        }
    }

    void save(JobOutput output) {
        File f = getFile(output.getId());
        try {
            mapper.writeValue(f, output);
        } catch (IOException e) {
            LOG.warn("Failed to write to {}", f.getAbsolutePath(), e);
        }
    }

    JobOutput create(Job job) {
        JobOutput jobOutput = new JobOutput();
        jobOutput.setId(job.getId());
        return jobOutput;
    }
}
