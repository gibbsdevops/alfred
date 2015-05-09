package com.gibbsdevops.alfred.model.alfred;

import com.gibbsdevops.alfred.model.github.GHCommit;

public class AlfredCommitNode {

    AlfredCommitProperties properties;

    private AlfredRepoNode repo;
    private AlfredGitUser committer;
    private AlfredGitUser author;
    private AlfredGitUser pusher;
    private AlfredUser sender;

    public static AlfredCommitNode from(AlfredCommitProperties properties) {
        AlfredCommitNode node = new AlfredCommitNode();
        node.properties = properties;
        return node;
    }

    public static AlfredCommitNode from(GHCommit commit) {
        AlfredCommitNode node = new AlfredCommitNode();
        node.properties = AlfredCommitProperties.from(commit);
        return node;
    }

    public AlfredCommit normalize() {
        AlfredCommit commit = new AlfredCommit();
        commit.properties = properties;
        commit.setRepoId(repo.getId());
        commit.setCommitterId(committer.getId());
        commit.setAuthorId(author.getId());
        commit.setPusherId(pusher.getId());
        commit.setSenderId(sender.getId());
        return commit;
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

    public String getHash() {
        return properties.getHash();
    }

    public void setHash(String id) {
        properties.setHash(id);
    }

    public String getMessage() {
        return properties.getMessage();
    }

    public void setMessage(String message) {
        properties.setMessage(message);
    }

    public long getTimestamp() {
        return properties.getTimestamp();
    }

    public void setTimestamp(long timestamp) {
        properties.setTimestamp(timestamp);
    }

    public int getAdditions() {
        return properties.getAdditions();
    }

    public void setAdditions(int additions) {
        properties.setAdditions(additions);
    }

    public int getDeletions() {
        return properties.getDeletions();
    }

    public void setDeletions(int deletions) {
        properties.setDeletions(deletions);
    }

    public AlfredRepoNode getRepo() {
        return repo;
    }

    public void setRepo(AlfredRepoNode repo) {
        this.repo = repo;
    }

    public AlfredGitUser getCommitter() {
        return committer;
    }

    public void setCommitter(AlfredGitUser committer) {
        this.committer = committer;
    }

    public AlfredGitUser getAuthor() {
        return author;
    }

    public void setAuthor(AlfredGitUser author) {
        this.author = author;
    }

    public AlfredGitUser getPusher() {
        return pusher;
    }

    public void setPusher(AlfredGitUser pusher) {
        this.pusher = pusher;
    }

    public AlfredUser getSender() {
        return sender;
    }

    public void setSender(AlfredUser sender) {
        this.sender = sender;
    }
    //</editor-fold>

}
