package com.gibbsdevops.alfred.model.events.local;

public class NewJobResponse {

    private NewJobRequest request;
    private int id;

    public NewJobRequest getRequest() {
        return request;
    }

    public void setRequest(NewJobRequest request) {
        this.request = request;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
