package com.gibbsdevops.alfred.service.build.impl;

import com.gibbsdevops.alfred.model.alfred.AlfredJobNode;
import com.gibbsdevops.alfred.service.build.BuildService;
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
    private BuildService buildService;

    public BuildInputStream(AlfredJobNode job, InputStream is, BuildService buildService) {
        super("BuildInputStream " + job);
        this.job = job;
        this.is = is;
        this.buildService = buildService;
    }

    public void run() {
        LOG.info("Starting BuildInputStream {}", job);

        try {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                buildService.logOutput(job, line);
            }
        } catch (IOException e) {
            LOG.warn("Error reading stream for {}", job, e);
        }
    }

}
