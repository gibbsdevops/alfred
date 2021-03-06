package com.gibbsdevops.alfred.model.alfred;

public class AlfredJobNode {

    public static AlfredJobNode from(AlfredJob job) {
        AlfredJobNode node = new AlfredJobNode();
        node.properties = job.properties;
        return node;
    }

    private AlfredJobProperties properties = new AlfredJobProperties();

    private AlfredCommitNode commit;

    public Long getId() {
        return properties.getId();
    }

    public void setId(Long id) {
        properties.setId(id);
    }

    public Integer getVersion() {
        return properties.getVersion();
    }

    public void setVersion(Integer version) {
        properties.setVersion(version);
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

    public AlfredCommitNode getCommit() {
        return commit;
    }

    public void setCommit(AlfredCommitNode commit) {
        this.commit = commit;
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

    public AlfredJob normalize() {
        AlfredJob job = new AlfredJob();
        job.properties = properties;
        job.setCommitId(getCommit().getId());
        return job;
    }

    @Override
    public String toString() {
        return "Job #" + getId();
    }

}
