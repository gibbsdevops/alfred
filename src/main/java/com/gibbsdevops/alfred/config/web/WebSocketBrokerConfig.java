package com.gibbsdevops.alfred.config.web;

import com.gibbsdevops.alfred.config.common.AlfredConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketBrokerConfig extends AbstractWebSocketMessageBrokerConfigurer {

    @Autowired
    private AlfredConfigProperties alfredConfig;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.setApplicationDestinationPrefixes("/app");
        // config.enableSimpleBroker("/topic"); // can breakout to ActiveMQ when ready

        config.enableStompBrokerRelay("/topic")
                .setRelayHost(alfredConfig.getStompBrokerHost())
                .setRelayPort(alfredConfig.getStompBrokerPort());
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/broker.socket").withSockJS(); // STOMP message broker
    }

}
