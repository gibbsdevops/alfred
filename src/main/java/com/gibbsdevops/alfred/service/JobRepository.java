package com.gibbsdevops.alfred.service;

import com.gibbsdevops.alfred.model.events.github.PushEvent;
import com.gibbsdevops.alfred.model.job.Job;

import java.util.Set;

public interface JobRepository {

    public Job create(PushEvent event);

    public void save(Job event);

    public Set<Job> getJobs();

}
