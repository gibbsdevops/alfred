package com.gibbsdevops.alfred.service.build.impl;

import com.gibbsdevops.alfred.model.alfred.AlfredJobNode;
import com.gibbsdevops.alfred.service.build.BuildStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class BuildInputStream extends Thread {

    private static final Logger LOG = LoggerFactory.getLogger(BuildInputStream.class);

    private AlfredJobNode job;
    private InputStream is;
    private BuildStatusService buildStatusService;

    public BuildInputStream(AlfredJobNode job, InputStream is, BuildStatusService buildStatusService) {
        super("BuildInputStream " + job);
        this.job = job;
        this.is = is;
        this.buildStatusService = buildStatusService;
    }

    public void run() {
        LOG.info("Starting BuildInputStream {}", job);

        try {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            int index = 0;
            while ((line = br.readLine()) != null) {
                buildStatusService.logOutput(job, index++, line);
            }
        } catch (IOException e) {
            LOG.warn("Error reading stream for {}", job, e);
        }
    }

}
