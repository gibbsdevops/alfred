package com.gibbsdevops.alfred.model.alfred;

import javax.persistence.*;

@Entity(name = "alfred_job")
public class AlfredJob extends AlfredJobProperties {

    AlfredJobProperties properties = new AlfredJobProperties();

    private Long commit;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Override
    public Integer getId() {
        return properties.getId();
    }

    @Override
    public void setId(Integer id) {
        properties.setId(id);
    }

    @Version
    @Override
    public Integer getVersion() {
        return properties.getVersion();
    }

    @Override
    public void setVersion(Integer version) {
        properties.setVersion(version);
    }

    public Long getCommit() {
        return commit;
    }

    public void setCommit(Long commit) {
        this.commit = commit;
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
}
