package com.gibbsdevops.alfred.service.build.impl;

import com.gibbsdevops.alfred.model.alfred.AlfredCommitNode;
import com.gibbsdevops.alfred.model.alfred.AlfredJobNode;
import com.gibbsdevops.alfred.model.alfred.AlfredRepoNode;
import com.gibbsdevops.alfred.service.build.BuildQueue;
import com.gibbsdevops.alfred.service.build.BuildService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;

@Service
public class LocalBuildQueue implements BuildQueue {

    private static final Logger LOG = LoggerFactory.getLogger(BuildServiceImpl.class);

    @Autowired
    @Qualifier("buildExecutor")
    private ExecutorService buildExecutor;
    
    @Autowired
    private BuildService buildService;

    @Override
    public void submit(AlfredJobNode job) {
        LOG.info("Submitted job {}", job);
        buildExecutor.execute(new BuildRunnable(job, buildService));
    }

}
