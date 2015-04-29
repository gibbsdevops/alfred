package com.gibbsdevops.alfred.model.alfred;

import com.gibbsdevops.alfred.model.github.GHRepository;

public class AlfredRepoNode {

    AlfredRepoProperties properties;

    private AlfredUser owner;

    public static AlfredRepoNode from(GHRepository repository) {
        AlfredRepoNode node = new AlfredRepoNode();
        node.properties = AlfredRepoProperties.from(repository);
        return node;
    }

    public static AlfredRepoNode from(AlfredRepoProperties repoProps) {
        AlfredRepoNode node = new AlfredRepoNode();
        node.properties = repoProps;
        return node;
    }

    public AlfredRepo normalize() {
        if (owner == null) throw new NullPointerException("Repo has no owner");
        if (owner.getId() == null) throw new NullPointerException("Owner has no ID");

        AlfredRepo repo = new AlfredRepo();
        repo.properties = properties;
        if (owner != null) {
            repo.setOwnerId(owner.getId());
        }
        return repo;
    }

    //<editor-fold desc="Getters and Setters">
    public Long getId() {
        return properties.getId();
    }

    public void setId(Long id) {
        properties.setId(id);
    }

    public Integer getVersion() {
        return properties.getVersion();
    }

    public void setVersion(Integer version) {
        properties.setVersion(version);
    }

    public Long getGithubId() {
        return properties.getGithubId();
    }

    public void setGithubId(Long githubId) {
        properties.setGithubId(githubId);
    }

    public String getName() {
        return properties.getName();
    }

    public void setName(String name) {
        properties.setName(name);
    }

    public String getFullName() {
        return properties.getFullName();
    }

    public void setFullName(String fullName) {
        properties.setFullName(fullName);
    }

    public Boolean isPriv() {
        return properties.isPriv();
    }

    public void setPriv(Boolean priv) {
        properties.setPriv(priv);
    }

    public String getDescription() {
        return properties.getDescription();
    }

    public void setDescription(String description) {
        properties.setDescription(description);
    }

    public Boolean isFork() {
        return properties.isFork();
    }

    public void setFork(Boolean fork) {
        properties.setFork(fork);
    }

    public String getUrl() {
        return properties.getUrl();
    }

    public void setUrl(String url) {
        properties.setUrl(url);
    }

    public String getHtmlUrl() {
        return properties.getHtmlUrl();
    }

    public void setHtmlUrl(String htmlUrl) {
        properties.setHtmlUrl(htmlUrl);
    }

    public String getSshUrl() {
        return properties.getSshUrl();
    }

    public void setSshUrl(String sshUrl) {
        properties.setSshUrl(sshUrl);
    }

    public String getGitUrl() {
        return properties.getGitUrl();
    }

    public void setGitUrl(String gitUrl) {
        properties.setGitUrl(gitUrl);
    }

    public String getCloneUrl() {
        return properties.getCloneUrl();
    }

    public void setCloneUrl(String cloneUrl) {
        properties.setCloneUrl(cloneUrl);
    }

    public Long getCreatedAt() {
        return properties.getCreatedAt();
    }

    public void setCreatedAt(Long createdAt) {
        properties.setCreatedAt(createdAt);
    }

    public Long getUpdatedAt() {
        return properties.getUpdatedAt();
    }

    public void setUpdatedAt(Long updatedAt) {
        properties.setUpdatedAt(updatedAt);
    }

    public Long getPushedAt() {
        return properties.getPushedAt();
    }

    public void setPushedAt(Long pushedAt) {
        properties.setPushedAt(pushedAt);
    }

    public String getHomepage() {
        return properties.getHomepage();
    }

    public void setHomepage(String homepage) {
        properties.setHomepage(homepage);
    }

    public String getLanguage() {
        return properties.getLanguage();
    }

    public void setLanguage(String language) {
        properties.setLanguage(language);
    }

    public String getDefaultBranch() {
        return properties.getDefaultBranch();
    }

    public void setDefaultBranch(String defaultBranch) {
        properties.setDefaultBranch(defaultBranch);
    }

    public AlfredUser getOwner() {
        return owner;
    }

    public void setOwner(AlfredUser owner) {
        this.owner = owner;
    }
    //</editor-fold>

}
