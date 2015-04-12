package com.gibbsdevops.alfred.model.alfred;

import com.gibbsdevops.alfred.model.github.GHCommit;

public class AlfredCommitNode extends AlfredCommitProperties {

    private AlfredCommitProperties properties;

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

    //<editor-fold desc="Getters and Setters">
    @Override
    public String getId() {
        return properties.getId();
    }

    @Override
    public void setId(String id) {
        properties.setId(id);
    }

    @Override
    public String getMessage() {
        return properties.getMessage();
    }

    @Override
    public void setMessage(String message) {
        properties.setMessage(message);
    }

    @Override
    public long getTimestamp() {
        return properties.getTimestamp();
    }

    @Override
    public void setTimestamp(long timestamp) {
        properties.setTimestamp(timestamp);
    }

    @Override
    public int getAdditions() {
        return properties.getAdditions();
    }

    @Override
    public void setAdditions(int additions) {
        properties.setAdditions(additions);
    }

    @Override
    public int getDeletions() {
        return properties.getDeletions();
    }

    @Override
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
