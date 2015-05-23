package com.gibbsdevops.alfred.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.ConnectionFactory;

@Configuration
public class MessagingConfig {

    @Bean
    public JmsTemplate jmsTemplate() {
        JmsTemplate template = new JmsTemplate();
        template.setConnectionFactory(messagingConnectionFactory());
        return template;
    }

    @Bean
    public ConnectionFactory messagingConnectionFactory() {
        brokerService();
        return new ActiveMQConnectionFactory("tcp://localhost:61613");
    }

    @Bean
    public BrokerService brokerService() {
        BrokerService broker = null;
        try {
            broker = BrokerFactory.createBroker("broker:stomp://localhost:61613");
            broker.start();
        } catch (Exception e) {
            throw new RuntimeException("Unable to start broker", e);
        }
        return broker;
    }

}
