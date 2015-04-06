package com.gibbsdevops.alfred.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gibbsdevops.alfred.model.events.github.PushEvent;
import com.gibbsdevops.alfred.service.ingest.IngestService;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping(value = "/ingest")
public class IngestApiController extends ApiController {

    private static final Logger LOG = LoggerFactory.getLogger(IngestApiController.class);
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final AtomicInteger counter = new AtomicInteger();

    @Autowired
    private IngestService ingestService;

    @RequestMapping(method = RequestMethod.GET)
    public Object status() {
        return generateStatus("ingest", "OK");
    }

    @RequestMapping(method = RequestMethod.POST, headers = {"X-Github-Event=push", "Accept=application/json"})
    public Object ingestJson(@RequestHeader(value = "X-Github-Delivery") String guid, @RequestBody JsonNode json) throws IOException {
        writeEvent(guid, json);

        PushEvent event = mapper.treeToValue(json, PushEvent.class);
        event.setGuid(guid);

        ingestService.handle(event);
        return ok();
    }

    @RequestMapping(method = RequestMethod.POST, headers = {"X-Github-Event=ping", "Accept=application/json"})
    public Object handlePing(@RequestHeader(value = "X-Github-Delivery") String guid) {
        LOG.info("Ping from {}", guid);

        Map<String, Object> root = new HashMap<>();
        root.put("ping", "OK");
        return root;
    }

    void writeEvent(String guid, JsonNode json) throws IOException {
        String filename = String.format("event_%d-%05d_%s.json", System.currentTimeMillis(), counter.getAndIncrement(), guid);
        File file = new File(new File("ingest"), filename);
        OutputStream os = new FileOutputStream(file);
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(os, json);
        } finally {
            os.close();
        }
    }
}