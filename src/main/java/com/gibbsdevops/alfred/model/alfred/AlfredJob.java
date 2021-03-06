package com.gibbsdevops.alfred.model.alfred;

import javax.persistence.*;

@Entity(name = "alfred_job")
public class AlfredJob {

    AlfredJobProperties properties = new AlfredJobProperties();

    private Long commitId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return properties.getId();
    }

    public void setId(Long id) {
        properties.setId(id);
    }

    @Version
    public Integer getVersion() {
        return properties.getVersion();
    }

    public void setVersion(Integer version) {
        properties.setVersion(version);
    }

    public Long getCommitId() {
        return commitId;
    }

    public void setCommitId(Long commitId) {
        this.commitId = commitId;
    }

    public String getStatus() {
        return properties.getStatus();
    }

    public void setStatus(String status) {
        properties.setStatus(status);
    }

    public String getError() {
        return properties.getError();
    }

    public void setError(String error) {
        properties.setError(error);
    }

    public Integer getDuration() {
        return properties.getDuration();
    }

    public Long getCreatedAt() {
        return properties.getCreatedAt();
    }

    public void setCreatedAt(Long createdAt) {
        properties.setCreatedAt(createdAt);
    }

    public void setDuration(Integer duration) {
        properties.setDuration(duration);
    }

    @Override
    public String toString() {
        return "Job #" + getId();
    }

}
