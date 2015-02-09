package com.gibbsdevops.alfred.model.job;

import com.google.common.collect.Lists;

import java.util.List;

public class JobOutput {

    private int id;
    private List<String> buildOutput = Lists.newArrayList();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getBuildOutput() {
        return buildOutput;
    }

}
