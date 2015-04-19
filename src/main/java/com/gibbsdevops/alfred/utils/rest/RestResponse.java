package com.gibbsdevops.alfred.utils.rest;

import com.gibbsdevops.alfred.model.alfred.utils.AlfredObjectMapperFactory;

import java.io.IOException;

public class RestResponse {

    private String body;

    public RestResponse(String body) {
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public <T> T as(Class<T> cls) {
        try {
            return AlfredObjectMapperFactory.get().readValue(body, cls);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse rest response. Body was: " + body, e);
        }
    }

}
