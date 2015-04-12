package com.gibbsdevops.alfred.model.alfred;

public class AlfredCommit extends AlfredCommitProperties {

    private AlfredCommitProperties properties;

    private long repo;
    private long committer;
    private long author;
    private long pusher;
    private long sender;

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

    public long getRepo() {
        return repo;
    }

    public void setRepo(long repo) {
        this.repo = repo;
    }

    public long getCommitter() {
        return committer;
    }

    public void setCommitter(long committer) {
        this.committer = committer;
    }

    public long getAuthor() {
        return author;
    }

    public void setAuthor(long author) {
        this.author = author;
    }

    public long getPusher() {
        return pusher;
    }

    public void setPusher(long pusher) {
        this.pusher = pusher;
    }

    public long getSender() {
        return sender;
    }

    public void setSender(long sender) {
        this.sender = sender;
    }
    //</editor-fold>

}
