package com.gibbsdevops.alfred.model.events.github;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PushEvent extends Event {

    private String ref;
    private GHPersonRef pusher;
    private String compare;
    private List<Commit> commits;
    @JsonProperty("head_commit")
    private Commit headCommit;
    private Repository repository;
    private GHOrganization organization;
    private GHPerson sender;

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public GHPersonRef getPusher() {
        return pusher;
    }

    public String getCompare() {
        return compare;
    }

    public void setCompare(String compare) {
        this.compare = compare;
    }

    public void setPusher(GHPersonRef pusher) {
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

    public GHOrganization getOrganization() {
        return organization;
    }

    public void setOrganization(GHOrganization organization) {
        this.organization = organization;
    }

    public GHPerson getSender() {
        return sender;
    }

    public void setSender(GHPerson sender) {
        this.sender = sender;
    }

}
