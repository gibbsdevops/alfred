package com.gibbsdevops.alfred.web.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gibbsdevops.alfred.model.alfred.utils.AlfredObjectMapperFactory;
import com.gibbsdevops.alfred.model.github.events.GHPushEvent;
import com.gibbsdevops.alfred.service.ingest.IngestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping(value = "/ingest")
public class IngestApiController extends ApiController {

    private static final Logger LOG = LoggerFactory.getLogger(IngestApiController.class);
    private static final ObjectMapper mapper = AlfredObjectMapperFactory.get();
    private static final AtomicInteger counter = new AtomicInteger();
    public static final File INGEST_PATH = new File("ingest");

    @PostConstruct
    public void init() {
        LOG.info("Initializing up IngestController");
        INGEST_PATH.mkdirs();
    }

    @Autowired
    private IngestService ingestService;

    @RequestMapping(method = RequestMethod.GET)
    public Object status() {
        return generateStatus("ingest", "OK");
    }

    @RequestMapping(method = RequestMethod.POST, headers = {"Accept=application/json"})
    public Object ingestJson(@RequestHeader(value = "X-Github-Delivery") String guid,
                             @RequestHeader(value = "X-Github-Event") String type,
                             @RequestBody JsonNode json) throws IOException {

        writeEvent(guid, type, json);

        if ("push".equals(type)) {
            GHPushEvent event = mapper.treeToValue(json, GHPushEvent.class);
            event.setGuid(guid);
            ingestService.handle(event);
        }

        return ok();
    }

    void writeEvent(String guid, String type, JsonNode json) throws IOException {
        String filename = String.format("event_%d-%05d_%s_%s.json", System.currentTimeMillis(), counter.getAndIncrement(), guid, type);
        File file = new File(INGEST_PATH, filename);
        OutputStream os = new FileOutputStream(file);
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(os, json);
        } finally {
            os.close();
        }
    }
}