package com.gibbsdevops.alfred.model.events.github;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PushEvent extends Event {

    private String ref;
    private User pusher;
    private List<Commit> commits;
    @JsonProperty("head_commit")
    private Commit headCommit;
    private Repository repository;
    private Organization organization;

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

    public List<Commit> getCommits() {
        return commits;
    }

    public void setCommits(List<Commit> commits) {
        this.commits = commits;
    }

    public Commit getHeadCommit() {
        return headCommit;
    }

    public void setHeadCommit(Commit headCommit) {
        this.headCommit = headCommit;
    }

    public Repository getRepository() {
        return repository;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

}
