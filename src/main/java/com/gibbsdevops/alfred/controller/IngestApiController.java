package com.gibbsdevops.alfred.controller;

import com.gibbsdevops.alfred.model.events.github.PushEvent;
import com.gibbsdevops.alfred.service.IngestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/ingest")
public class IngestApiController extends ApiController {

    private static final Logger LOG = LoggerFactory.getLogger(IngestApiController.class);

    @Autowired
    private IngestService ingestService;

    @RequestMapping(method = RequestMethod.GET)
    public Object status() {
        return generateStatus("ingest", "OK");
    }

    @RequestMapping(method = RequestMethod.POST, headers = {"X-Github-Event=push", "Accept=application/json"})
    public Object ingestJson(@RequestHeader(value = "X-Github-Delivery") String guid, @RequestBody PushEvent event) {
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

}