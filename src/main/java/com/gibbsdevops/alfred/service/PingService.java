package com.gibbsdevops.alfred.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PingService {

    private static final Logger LOG = LoggerFactory.getLogger(PingService.class);

    @Autowired
    private SimpMessagingTemplate template;

    @Scheduled(fixedDelay=1000)
    public void ping() {
        LOG.info("xxx");
        template.convertAndSend("/topic/ping", "ping");
    }

}
