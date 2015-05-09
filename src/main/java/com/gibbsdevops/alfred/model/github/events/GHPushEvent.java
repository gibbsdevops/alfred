package com.gibbsdevops.alfred.model.github.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gibbsdevops.alfred.model.github.*;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GHPushEvent extends GHEvent {

    private String ref;
    private GHPersonRef pusher;
    private String compare;
    private List<GHCommit> commits;
    @JsonProperty("head_commit")
    private GHCommit headCommit;
    private GHHookRepository repository;
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

    public List<GHCommit> getCommits() {
        return commits;
    }

    public void setCommits(List<GHCommit> commits) {
        this.commits = commits;
    }

    public GHCommit getHeadCommit() {
        return headCommit;
    }

    public void setHeadCommit(GHCommit headCommit) {
        this.headCommit = headCommit;
    }

    public GHHookRepository getRepository() {
        return repository;
    }

    public void setRepository(GHHookRepository repository) {
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
