package com.gibbsdevops.alfred.model.alfred;

import com.gibbsdevops.alfred.model.github.GHCommit;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

public class AlfredCommitProperties {

    private String hash;
    private String message;
    private long timestamp;
    private int additions;
    private int deletions;

    public static AlfredCommitProperties from(GHCommit commit) {
        AlfredCommitProperties props = new AlfredCommitProperties();
        props.hash = commit.getId();
        props.message = commit.getMessage();

        TemporalAccessor parsed = DateTimeFormatter.ISO_OFFSET_DATE_TIME.parse(commit.getTimestamp());
        Instant instant = Instant.from(parsed);

        props.timestamp = instant.getEpochSecond();

        return props;
    }

    //<editor-fold desc="Getters and Setters">
    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
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
