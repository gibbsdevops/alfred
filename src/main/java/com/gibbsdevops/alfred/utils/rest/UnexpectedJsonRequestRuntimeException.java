package com.gibbsdevops.alfred.utils.rest;

public class UnexpectedJsonRequestRuntimeException extends RuntimeException {

    private RestRequest request;

    public UnexpectedJsonRequestRuntimeException(RestRequest request) {
        this.request = request;
    }

    public RestRequest getRequest() {
        return request;
    }

    @Override
    public String getMessage() {
        return request.getType().name() + " " + request.getUrl();
    }

}
