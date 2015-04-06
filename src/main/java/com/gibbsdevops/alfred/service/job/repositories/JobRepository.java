package com.gibbsdevops.alfred.service.job.repositories;

import com.gibbsdevops.alfred.model.job.Job;

import java.util.Set;

public interface JobRepository {

    void save(Job event);

    Set<Job> getJobs();

    Job getJob(int id);

}
