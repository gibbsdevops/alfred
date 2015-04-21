package com.gibbsdevops.alfred.model.alfred;

public class AlfredJobNode extends AlfredJobProperties {

    private AlfredJobProperties properties = new AlfredJobProperties();

    private AlfredCommitNode commit;

    @Override
    public Integer getId() {
        return properties.getId();
    }

    @Override
    public void setId(Integer id) {
        properties.setId(id);
    }

    @Override
    public Integer getVersion() {
        return properties.getVersion();
    }

    @Override
    public void setVersion(Integer version) {
        properties.setVersion(version);
    }

    @Override
    public String getStatus() {
        return properties.getStatus();
    }

    @Override
    public void setStatus(String status) {
        properties.setStatus(status);
    }

    @Override
    public String getError() {
        return properties.getError();
    }

    @Override
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
        job.setCommit(commit.getId());
        return job;
    }

}
