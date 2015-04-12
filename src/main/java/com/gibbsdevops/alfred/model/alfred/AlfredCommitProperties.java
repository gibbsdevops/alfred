package com.gibbsdevops.alfred.model.alfred;

import com.gibbsdevops.alfred.model.github.GHCommit;
import com.gibbsdevops.alfred.model.github.utils.GHTimeFormat;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

public class AlfredCommitProperties {

    private String id;
    private String message;
    private long timestamp;
    private int additions;
    private int deletions;

    public static AlfredCommitProperties from(GHCommit commit) {
        AlfredCommitProperties props = new AlfredCommitProperties();
        props.id = commit.getId();
        props.message = commit.getMessage();
        props.timestamp = Instant.from(DateTimeFormatter.ISO_OFFSET_DATE_TIME.parse(
                commit.getTimestamp())).getEpochSecond();
        return props;
    }

    //<editor-fold desc="Getters and Setters">
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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getAdditions() {
        return additions;
    }

    public void setAdditions(int additions) {
        this.additions = additions;
    }

    public int getDeletions() {
        return deletions;
    }

    public void setDeletions(int deletions) {
        this.deletions = deletions;
    }
    //</editor-fold>

}