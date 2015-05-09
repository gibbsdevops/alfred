package com.gibbsdevops.alfred.model.alfred;

import javax.persistence.*;

@Entity(name = "alfred_commit")
public class AlfredCommit {

    AlfredCommitProperties properties = new AlfredCommitProperties();

    private Long repoId;
    private Long committerId;
    private Long authorId;
    private Long pusherId;
    private Long senderId;

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

    public Long getRepoId() {
        return repoId;
    }

    public void setRepoId(Long repoId) {
        this.repoId = repoId;
    }

    public Long getCommitterId() {
        return committerId;
    }

    public void setCommitterId(Long committerId) {
        this.committerId = committerId;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public Long getPusherId() {
        return pusherId;
    }

    public void setPusherId(Long pusherId) {
        this.pusherId = pusherId;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }
    //</editor-fold>

}
