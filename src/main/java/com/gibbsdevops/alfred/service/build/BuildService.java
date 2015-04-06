package com.gibbsdevops.alfred.service.build;

import com.gibbsdevops.alfred.web.model.job.Job;

public interface BuildService {

    public void submit(Job job);

    public void starting(Job job);

    public void finished(Job job);

    void failed(Job job, String reason);

    void logOutput(Job job, String line);

}
