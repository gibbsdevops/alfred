package com.gibbsdevops.alfred.model.alfred;

import javax.persistence.*;

@Entity(name = "alfred_job")
public class AlfredJob {

    AlfredJobProperties properties = new AlfredJobProperties();

    private Long commit;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return properties.getId();
    }

    public void setId(Integer id) {
        properties.setId(id);
    }

    @Version
    public Integer getVersion() {
        return properties.getVersion();
    }

    public void setVersion(Integer version) {
        properties.setVersion(version);
    }

    public Long getCommit() {
        return commit;
    }

    public void setCommit(Long commit) {
        this.commit = commit;
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
}
