package com.gibbsdevops.alfred.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.core.MessageSendingOperations;
import org.springframework.messaging.simp.broker.BrokerAvailabilityEvent;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PingService implements ApplicationListener<BrokerAvailabilityEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(PingService.class);

    private boolean brokerAvailable = false;

    @Autowired
    private MessageSendingOperations<String> template;

    @Scheduled(fixedDelay = 2000)
    public void ping() {
        if (brokerAvailable) {
            template.convertAndSend("/topic/ping", System.currentTimeMillis());
        } else {
            LOG.warn("Unable to send ping. Broker not available");
        }
    }

    @Override
    public void onApplicationEvent(BrokerAvailabilityEvent event) {
        LOG.info("BrokerAvailabilityEvent: {}", event.toString());
        brokerAvailable = event.isBrokerAvailable();
    }

}
