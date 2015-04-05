package com.gibbsdevops.alfred.model.events.local;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NewJobRequest {

    private String repo;
    private String organization;
    private String spec;
    @JsonProperty("html_url")
    private String htmlUrl;
    @JsonProperty("ssh_url")
    private String sshUrl;
    @JsonProperty("git_url")
    private String gitUrl;
    @JsonProperty("clone_url")
    private String cloneUrl;

    public String getRepo() {
        return repo;
    }

    public void setRepo(String repo) {
        this.repo = repo;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    public String getSshUrl() {
        return sshUrl;
    }

    public void setSshUrl(String sshUrl) {
        this.sshUrl = sshUrl;
    }

    public String getGitUrl() {
        return gitUrl;
    }

    public void setGitUrl(String gitUrl) {
        this.gitUrl = gitUrl;
    }

    public String getCloneUrl() {
        return cloneUrl;
    }

    public void setCloneUrl(String cloneUrl) {
        this.cloneUrl = cloneUrl;
    }

}
