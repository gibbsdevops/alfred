package com.gibbsdevops.alfred.service.job;

import com.gibbsdevops.alfred.web.model.job.Job;

import java.util.List;

public interface JobService {

    void save(Job job);

    void appendOutput(Job job, String line);

    List<Job> getJobs();

    Job getJob(int id);

}
