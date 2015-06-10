package com.gibbsdevops.alfred.config.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gibbsdevops.alfred.model.alfred.utils.AlfredObjectMapperFactory;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.command.ActiveMQQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;

import javax.jms.*;
import java.io.IOException;

@Configuration
@EnableJms
public class JmsConfig {

    private static final Logger LOG = LoggerFactory.getLogger(JmsConfig.class);

    public static final String JOB_BUILD_QUEUE_NAME = "job.build.queue";
    public static final String JOB_STATUS_QUEUE_NAME = "job.status.queue";
    public static final String JOB_LOG_QUEUE_NAME = "job.log.queue";

    @Autowired
    private AlfredConfigProperties config;

    @Bean
    public Queue jobBuildQueue() {
        return new ActiveMQQueue(JOB_BUILD_QUEUE_NAME);
    }

    @Bean
    public Queue jobStatusQueue() {
        return new ActiveMQQueue(JOB_STATUS_QUEUE_NAME);
    }

    @Bean
    public Queue jobLogQueue() {
        return new ActiveMQQueue(JOB_LOG_QUEUE_NAME);
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        if (config.isRunEmbeddedBroker()) {
            LOG.info("Starting embedded JMS broker");

            BrokerService broker = null;
            try {
                broker = BrokerFactory.createBroker("broker:stomp://localhost:61613,tcp://localhost:61616");
                broker.start();
            } catch (Exception e) {
                throw new RuntimeException("Unable to start embedded broker", e);
            }
        }
        return new ActiveMQConnectionFactory(config.getJmsBrokerUrl());
    }

    @Bean
    public JmsTemplate jmsTemplate() {
        JmsTemplate template = new JmsTemplate();
        template.setConnectionFactory(connectionFactory());
        return template;
    }

}
