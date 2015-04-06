package com.gibbsdevops.alfred.service.job.repositories.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gibbsdevops.alfred.web.model.events.job.JobLine;
import com.gibbsdevops.alfred.web.model.job.JobOutput;
import com.gibbsdevops.alfred.service.job.repositories.JobOutputRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class JobOutputRepositoryImpl implements JobOutputRepository {

    private static final Logger LOG = LoggerFactory.getLogger(FileJobRepository.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private SimpMessagingTemplate template;

    @Override
    public void append(int id, String line) {
        JobOutput output = load(id);
        output.getOutput().add(line);
        save(output);

        JobLine jobLine = new JobLine();
        jobLine.setId(id);
        jobLine.setIndex(output.getOutput().size() - 1);
        jobLine.setLine(line);

        LOG.info("Sending JobLine to /topic/job-line: {}", id);
        template.convertAndSend("/topic/job-line", jobLine);
    }

    @Override
    public JobOutput get(int id) {
        return load(id);
    }

    File getFile(int id) {
        new File("jobs").mkdirs();
        return new File(new File("jobs"), String.format("%07d", id) + "-output.json");
    }

    JobOutput load(int id) {
        File f = getFile(id);

        if (!f.exists()) {
            return create(id);
        }

        try {
            return mapper.readValue(f, JobOutput.class);
        } catch (IOException e) {
            LOG.warn("Failed to read {}", f.getAbsolutePath(), e);
            return create(id);
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

    JobOutput create(int id) {
        JobOutput jobOutput = new JobOutput();
        jobOutput.setId(id);
        return jobOutput;
    }
}
