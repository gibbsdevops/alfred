package com.gibbsdevops.alfred.model.alfred;

public class AlfredJobNode {

    private AlfredJobProperties properties = new AlfredJobProperties();

    private AlfredCommitNode commit;

    public Integer getId() {
        return properties.getId();
    }

    public void setId(Integer id) {
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

    public AlfredJob normalize() {
        AlfredJob job = new AlfredJob();
        job.properties = properties;
        job.setCommit(getCommit().getId());
        return job;
    }

}
