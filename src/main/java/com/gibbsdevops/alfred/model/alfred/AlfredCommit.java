package com.gibbsdevops.alfred.model.alfred;

import javax.persistence.*;

@Entity(name = "alfred_commit")
public class AlfredCommit {

    AlfredCommitProperties properties = new AlfredCommitProperties();

    private Long repo;
    private Long committer;
    private Long author;
    private Long pusher;
    private Long sender;

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

    public Long getRepo() {
        return repo;
    }

    public void setRepo(Long repo) {
        this.repo = repo;
    }

    public Long getCommitter() {
        return committer;
    }

    public void setCommitter(Long committer) {
        this.committer = committer;
    }

    public Long getAuthor() {
        return author;
    }

    public void setAuthor(Long author) {
        this.author = author;
    }

    public Long getPusher() {
        return pusher;
    }

    public void setPusher(Long pusher) {
        this.pusher = pusher;
    }

    public Long getSender() {
        return sender;
    }

    public void setSender(Long sender) {
        this.sender = sender;
    }
    //</editor-fold>

}
