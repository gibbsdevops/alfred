package com.gibbsdevops.alfred.service.job.repositories;

import com.gibbsdevops.alfred.model.events.github.PushEvent;
import com.gibbsdevops.alfred.model.job.Job;

import java.util.Set;

public interface JobRepository {

    public void save(Job event);

    public Set<Job> getJobs();

}
