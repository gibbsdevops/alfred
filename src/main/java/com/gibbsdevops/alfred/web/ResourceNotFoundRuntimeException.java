package com.gibbsdevops.alfred.web;

public class ResourceNotFoundRuntimeException extends RuntimeException {

    public ResourceNotFoundRuntimeException() {
    }

    public ResourceNotFoundRuntimeException(String message) {
        super(message);
    }

}
