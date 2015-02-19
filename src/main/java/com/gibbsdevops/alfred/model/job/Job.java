package com.gibbsdevops.alfred.model.job;

import com.gibbsdevops.alfred.model.events.github.Commit;
import com.gibbsdevops.alfred.model.events.github.Organization;
import com.gibbsdevops.alfred.model.events.github.Repository;
import com.gibbsdevops.alfred.model.events.github.User;

public class Job {

    private Integer id;
    private Organization organization;
    private Repository repository;
    private Commit commit;
    private String ref;
    private User pusher;
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

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public Repository getRepository() {
        return repository;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    public Commit getCommit() {
        return commit;
    }

    public void setCommit(Commit commit) {
        this.commit = commit;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public User getPusher() {
        return pusher;
    }

    public void setPusher(User pusher) {
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
