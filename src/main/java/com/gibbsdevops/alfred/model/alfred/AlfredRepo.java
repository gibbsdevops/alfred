package com.gibbsdevops.alfred.model.alfred;

import javax.persistence.*;

@Entity(name = "alfred_repo")
public class AlfredRepo {

    AlfredRepoProperties properties = new AlfredRepoProperties();

    private Long owner;

    //<editor-fold desc="Getters and Setters">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return properties.getId();
    }

    public void setId(Long id) {
        properties.setId(id);
    }

    @Version
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

    @Column(name = "private")
    public boolean isPriv() {
        return properties.isPriv();
    }

    public void setPriv(boolean priv) {
        properties.setPriv(priv);
    }

    public String getDescription() {
        return properties.getDescription();
    }

    public void setDescription(String description) {
        properties.setDescription(description);
    }

    public boolean isFork() {
        return properties.isFork();
    }

    public void setFork(boolean fork) {
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

    public long getCreatedAt() {
        return properties.getCreatedAt();
    }

    public void setCreatedAt(long createdAt) {
        properties.setCreatedAt(createdAt);
    }

    public long getUpdatedAt() {
        return properties.getUpdatedAt();
    }

    public void setUpdatedAt(long updatedAt) {
        properties.setUpdatedAt(updatedAt);
    }

    public long getPushedAt() {
        return properties.getPushedAt();
    }

    public void setPushedAt(long pushedAt) {
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

    public Long getOwner() {
        return owner;
    }

    public void setOwner(Long owner) {
        this.owner = owner;
    }
    //</editor-fold>

}
