package com.gibbsdevops.alfred.service.job;

import com.gibbsdevops.alfred.model.job.Job;

import java.util.List;

public interface JobService {

    void save(Job job);

    List<Job> getJobs();

    Job getJob(int id);

}
