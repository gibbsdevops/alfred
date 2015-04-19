package com.gibbsdevops.alfred.model.alfred;

import com.gibbsdevops.alfred.model.github.GHRepository;

public class AlfredRepoNode extends AlfredRepoProperties {

    private AlfredRepoProperties properties;

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
            repo.setOwner(owner.getId());
        }
        return repo;
    }

    //<editor-fold desc="Getters and Setters">
    @Override
    public Long getId() {
        return properties.getId();
    }

    @Override
    public void setId(Long id) {
        properties.setId(id);
    }

    @Override
    public Integer getVersion() {
        return properties.getVersion();
    }

    @Override
    public void setVersion(Integer version) {
        properties.setVersion(version);
    }

    @Override
    public Long getGithubId() {
        return properties.getGithubId();
    }

    @Override
    public void setGithubId(Long githubId) {
        properties.setGithubId(githubId);
    }

    @Override
    public String getName() {
        return properties.getName();
    }

    @Override
    public void setName(String name) {
        properties.setName(name);
    }

    @Override
    public String getFullName() {
        return properties.getFullName();
    }

    @Override
    public void setFullName(String fullName) {
        properties.setFullName(fullName);
    }

    @Override
    public boolean isPriv() {
        return properties.isPriv();
    }

    @Override
    public void setPriv(boolean priv) {
        properties.setPriv(priv);
    }

    @Override
    public String getDescription() {
        return properties.getDescription();
    }

    @Override
    public void setDescription(String description) {
        properties.setDescription(description);
    }

    @Override
    public boolean isFork() {
        return properties.isFork();
    }

    @Override
    public void setFork(boolean fork) {
        properties.setFork(fork);
    }

    @Override
    public String getUrl() {
        return properties.getUrl();
    }

    @Override
    public void setUrl(String url) {
        properties.setUrl(url);
    }

    @Override
    public String getHtmlUrl() {
        return properties.getHtmlUrl();
    }

    @Override
    public void setHtmlUrl(String htmlUrl) {
        properties.setHtmlUrl(htmlUrl);
    }

    @Override
    public String getSshUrl() {
        return properties.getSshUrl();
    }

    @Override
    public void setSshUrl(String sshUrl) {
        properties.setSshUrl(sshUrl);
    }

    @Override
    public String getGitUrl() {
        return properties.getGitUrl();
    }

    @Override
    public void setGitUrl(String gitUrl) {
        properties.setGitUrl(gitUrl);
    }

    @Override
    public String getCloneUrl() {
        return properties.getCloneUrl();
    }

    @Override
    public void setCloneUrl(String cloneUrl) {
        properties.setCloneUrl(cloneUrl);
    }

    @Override
    public long getCreatedAt() {
        return properties.getCreatedAt();
    }

    @Override
    public void setCreatedAt(long createdAt) {
        properties.setCreatedAt(createdAt);
    }

    @Override
    public long getUpdatedAt() {
        return properties.getUpdatedAt();
    }

    @Override
    public void setUpdatedAt(long updatedAt) {
        properties.setUpdatedAt(updatedAt);
    }

    @Override
    public long getPushedAt() {
        return properties.getPushedAt();
    }

    @Override
    public void setPushedAt(long pushedAt) {
        properties.setPushedAt(pushedAt);
    }

    @Override
    public String getHomepage() {
        return properties.getHomepage();
    }

    @Override
    public void setHomepage(String homepage) {
        properties.setHomepage(homepage);
    }

    @Override
    public String getLanguage() {
        return properties.getLanguage();
    }

    @Override
    public void setLanguage(String language) {
        properties.setLanguage(language);
    }

    @Override
    public String getDefaultBranch() {
        return properties.getDefaultBranch();
    }

    @Override
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
