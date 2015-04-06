package com.gibbsdevops.alfred.web.model.events.github;

public abstract class Event {

    private String guid;

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    @Override
    public String toString() {
        return "GitHub Event " + guid;
    }

}
