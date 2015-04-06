package com.gibbsdevops.alfred.web.controller;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(value = "/settings")
public class SettingsController extends ApiController {

    private static final Logger LOG = LoggerFactory.getLogger(SettingsController.class);

    @RequestMapping("/system")
    public Object status() {
        Map<String, Object> settings = Maps.newHashMap();
        settings.put("websocket-uri", "/api/broker.socket");
        return settings;
    }

}
