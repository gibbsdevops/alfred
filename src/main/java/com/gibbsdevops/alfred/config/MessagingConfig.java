package com.gibbsdevops.alfred.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.ConnectionFactory;
import javax.jms.Queue;

@Configuration
@EnableJms
public class MessagingConfig {

    public static final String JOB_QUEUE_NAME = "job.queue";

    @Bean
    public Queue jobQueue() {
        return new ActiveMQQueue(JOB_QUEUE_NAME);
    }

    @Bean
    public JmsTemplate jmsTemplate() {
        JmsTemplate template = new JmsTemplate();
        template.setConnectionFactory(connectionFactory());
        return template;
    }

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
        DefaultJmsListenerContainerFactory factory =
                new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        factory.setConcurrency("2");
        return factory;
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        brokerService();
        return new ActiveMQConnectionFactory("tcp://localhost:61616");
    }

    @Bean
    public BrokerService brokerService() {
        BrokerService broker = null;
        try {
            broker = BrokerFactory.createBroker("broker:stomp://localhost:61613,tcp://localhost:61616");
            broker.start();
        } catch (Exception e) {
            throw new RuntimeException("Unable to start broker", e);
        }
        return broker;
    }

}
