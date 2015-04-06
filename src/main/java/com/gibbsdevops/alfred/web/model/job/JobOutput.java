package com.gibbsdevops.alfred.web.model.job;

import com.google.common.collect.Lists;

import java.util.List;

public class JobOutput {

    private int id;
    private List<String> output = Lists.newArrayList();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getOutput() {
        return output;
    }

}
