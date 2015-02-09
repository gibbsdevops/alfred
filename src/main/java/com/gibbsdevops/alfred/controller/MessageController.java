package com.gibbsdevops.alfred.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {

    private static final Logger LOG = LoggerFactory.getLogger(MessageController.class);

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public String greeting(String message) throws Exception {
        LOG.info("MessageController Received message {}", message);
        Thread.sleep(3000); // simulated delay
        return "Hello !!";
    }

}
