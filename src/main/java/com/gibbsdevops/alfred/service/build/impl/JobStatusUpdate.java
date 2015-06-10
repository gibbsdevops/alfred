package com.gibbsdevops.alfred.service.build.impl;

public class JobStatusUpdate {

    private Long id;
    private String status;
    private String description;
    private Integer duration;

    public JobStatusUpdate() {
    }

    public JobStatusUpdate(Long id, String status, String description, Integer duration) {
        this.id = id;
        this.status = status;
        this.description = description;
        this.duration = duration;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

}
