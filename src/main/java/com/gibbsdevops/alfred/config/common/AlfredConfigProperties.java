package com.gibbsdevops.alfred.config.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

@Component
public class AlfredConfigProperties {

    @Bean
    public static PropertySourcesPlaceholderConfigurer properties() {
        PropertySourcesPlaceholderConfigurer pspc = new PropertySourcesPlaceholderConfigurer();
        pspc.setLocations(
                new ClassPathResource("default.properties"),
                new FileSystemResource("alfred.properties")
        );
        pspc.setIgnoreResourceNotFound(true);
        pspc.setLocalOverride(true);
        return pspc;
    }

    @Value("${http.port:8080}")
    private String httpPort;

    public int getHttpPort() {
        return Integer.parseInt(httpPort);
    }

    @Value("${jms.broker.url:tcp://localhost:61616}")
    private String jmsBrokerUrl;

    public String getJmsBrokerUrl() {
        return jmsBrokerUrl;
    }

    @Value("${jms.broker.embedded:false}")
    private String runEmbeddedBroker;

    public boolean isRunEmbeddedBroker() {
        return Boolean.parseBoolean(runEmbeddedBroker);
    }

    @Value("${stomp.broker.host:127.0.0.1}")
    private String stompBrokerHost;

    public String getStompBrokerHost() {
        return stompBrokerHost;
    }

    @Value("${stomp.broker.port:61613}")
    private String stompBrokerPort;

    public int getStompBrokerPort() {
        return Integer.parseInt(stompBrokerPort);
    }

}
