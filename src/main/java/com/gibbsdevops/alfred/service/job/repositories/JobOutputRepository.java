package com.gibbsdevops.alfred.service.job.repositories;

import com.gibbsdevops.alfred.model.job.JobOutput;

public interface JobOutputRepository {

    void append(int id, String line);

    JobOutput get(int id);

}
