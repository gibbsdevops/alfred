package com.gibbsdevops.alfred.model.alfred;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gibbsdevops.alfred.model.github.GHRepository;
import com.gibbsdevops.alfred.model.github.utils.GHTimeFormat;

public class AlfredRepoProperties {

    private Long id;
    private Integer version;
    private Long githubId;
    private String name;
    private String fullName;
    @JsonProperty("private")
    private Boolean priv;
    private String description;
    private Boolean fork;
    private String url;
    private String htmlUrl;
    private String sshUrl;
    private String gitUrl;
    private String cloneUrl;
    private Long githubCreatedAt;
    private Long githubUpdatedAt;
    private Long pushedAt;
    private String homepage;
    private String language;
    private String defaultBranch;

    public static AlfredRepoProperties from(GHRepository repository) {
        AlfredRepoProperties properties = new AlfredRepoProperties();
        properties.setGithubId(repository.getId());
        properties.setName(repository.getName());
        properties.setFullName(repository.getFullName());
        properties.setPriv(repository.isPriv());
        properties.setDescription(repository.getDescription());
        properties.setFork(repository.isFork());
        properties.setUrl(extractUrlFromForksUrl(repository.getForksUrl()));
        properties.setHtmlUrl(repository.getHtmlUrl());
        properties.setSshUrl(repository.getSshUrl());
        properties.setGitUrl(repository.getGitUrl());
        properties.setCloneUrl(repository.getCloneUrl());
        properties.setGithubCreatedAt(GHTimeFormat.parse(repository.getCreatedAt()));
        properties.setGithubUpdatedAt(GHTimeFormat.parse(repository.getUpdatedAt()));
        properties.setPushedAt(GHTimeFormat.parse(repository.getPushedAt()));
        properties.setHomepage(repository.getHomepage());
        properties.setLanguage(repository.getLanguage());
        properties.setDefaultBranch(repository.getDefaultBranch());
        return properties;
    }

    public static AlfredRepoNode fromRepo(AlfredRepo repo) {
        AlfredRepoNode node = new AlfredRepoNode();
        node.properties = repo.properties;
        return node;
    }

    public static String extractUrlFromForksUrl(String forksUrl) {
        return forksUrl.substring(0, forksUrl.length() - 6);
    }

    //<editor-fold desc="Getters and Setters">
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Long getGithubId() {
        return githubId;
    }

    public void setGithubId(Long githubId) {
        this.githubId = githubId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Boolean isPriv() {
        return priv;
    }

    public void setPriv(Boolean priv) {
        this.priv = priv;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isFork() {
        return fork;
    }

    public void setFork(Boolean fork) {
        this.fork = fork;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public Long getGithubCreatedAt() {
        return githubCreatedAt;
    }

    public void setGithubCreatedAt(Long githubCreatedAt) {
        this.githubCreatedAt = githubCreatedAt;
    }

    public Long getGithubUpdatedAt() {
        return githubUpdatedAt;
    }

    public void setGithubUpdatedAt(Long githubUpdatedAt) {
        this.githubUpdatedAt = githubUpdatedAt;
    }

    public Long getPushedAt() {
        return pushedAt;
    }

    public void setPushedAt(Long pushedAt) {
        this.pushedAt = pushedAt;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDefaultBranch() {
        return defaultBranch;
    }

    public void setDefaultBranch(String defaultBranch) {
        this.defaultBranch = defaultBranch;
    }
    //</editor-fold>

    @Override
    public String toString() {
        return "AlfredRepoProperties{" +
                "id=" + getId() +
                ", fullName='" + getFullName() + '\'' +
                '}';
    }
}
