package com.gibbsdevops.alfred.service;

import com.gibbsdevops.alfred.model.job.Job;

public interface JobOutputRepository {

    public void append(Job job, String line);

}
