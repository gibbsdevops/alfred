package com.gibbsdevops.alfred.model.github;

public class GHCommit {

    private String id;
    private String message;
    private String timestamp;
    private GHPersonRef author;
    private GHPersonRef committer;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public GHPersonRef getAuthor() {
        return author;
    }

    public void setAuthor(GHPersonRef author) {
        this.author = author;
    }

    public GHPersonRef getCommitter() {
        return committer;
    }

    public void setCommitter(GHPersonRef committer) {
        this.committer = committer;
    }

}
