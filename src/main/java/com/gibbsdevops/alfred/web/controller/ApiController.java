package com.gibbsdevops.alfred.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class ApiController {

    private static final Logger LOG = LoggerFactory.getLogger(IngestApiController.class);

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Map exceptionHandler(HttpServletRequest req, Exception ex) {
        LOG.warn("Caught exception", ex);
        return Collections.singletonMap("error", ex.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map notFound() {
        return Collections.singletonMap("error", "not found");
    }

    Object ok() {
        Map<String, Object> root = new HashMap<>();
        root.put("OK", true);
        return root;
    }

    public Object generateStatus(String service, String status) {
        Map<String, Object> root = new HashMap<>();
        root.put("status", status);
        root.put("service", service);
        return root;
    }

}
