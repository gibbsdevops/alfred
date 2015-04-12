package com.gibbsdevops.alfred.model.alfred;

public class AlfredCommitNode extends AlfredCommitProperties {

    private AlfredCommitProperties properties;

    private AlfredRepoNode repo;
    private AlfredUser committer;
    private AlfredUser author;

    public static AlfredCommitNode from(AlfredCommitProperties properties) {
        AlfredCommitNode node = new AlfredCommitNode();
        node.properties = properties;
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

    public AlfredUser getCommitter() {
        return committer;
    }

    public void setCommitter(AlfredUser committer) {
        this.committer = committer;
    }

    public AlfredUser getAuthor() {
        return author;
    }

    public void setAuthor(AlfredUser author) {
        this.author = author;
    }
    //</editor-fold>

}
