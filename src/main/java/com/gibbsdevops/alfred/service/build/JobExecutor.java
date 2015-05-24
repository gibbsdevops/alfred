package com.gibbsdevops.alfred.service.build;

import com.gibbsdevops.alfred.model.alfred.AlfredJobNode;
import com.gibbsdevops.alfred.model.job.Job;

public interface JobExecutor {

    void execute(AlfredJobNode job);

}
