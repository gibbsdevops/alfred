package com.gibbsdevops.alfred.model.job;

import com.gibbsdevops.alfred.model.github.GHCommit;
import com.gibbsdevops.alfred.model.github.GHOrganization;
import com.gibbsdevops.alfred.model.github.GHPersonRef;
import com.gibbsdevops.alfred.model.github.GHRepository;

public class Job {

    private Integer id;
    private Integer version;
    private GHOrganization organization;
    private GHRepository repository;
    private GHCommit commit;
    private String ref;
    private GHPersonRef pusher;
    private String status;
    private String error;

    @Override
    public String toString() {
        return "#" + id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public GHOrganization getOrganization() {
        return organization;
    }

    public void setOrganization(GHOrganization organization) {
        this.organization = organization;
    }

    public GHRepository getRepository() {
        return repository;
    }

    public void setRepository(GHRepository repository) {
        this.repository = repository;
    }

    public GHCommit getCommit() {
        return commit;
    }

    public void setCommit(GHCommit commit) {
        this.commit = commit;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public GHPersonRef getPusher() {
        return pusher;
    }

    public void setPusher(GHPersonRef pusher) {
        this.pusher = pusher;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

}
