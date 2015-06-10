package com.gibbsdevops.alfred.config.web;

import com.gibbsdevops.alfred.config.common.AlfredConfigProperties;
import com.gibbsdevops.alfred.config.common.JmsConfig;
import com.gibbsdevops.alfred.service.build.impl.JsmBuildStatusListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;

import javax.jms.ConnectionFactory;

@Configuration
@Import({AlfredConfigProperties.class, JmsConfig.class})
@ComponentScan({
        "com.gibbsdevops.alfred.service",
        "com.gibbsdevops.alfred.repository",
        "com.gibbsdevops.alfred.utils.rest"
})
@EnableJpaRepositories("com.gibbsdevops.alfred.dao")
public class WebConfig {

    @Autowired
    private ConnectionFactory connectionFactory;

    @Bean
    public DefaultJmsListenerContainerFactory defaultJmsContainerFactory() {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setConcurrency("4");
        return factory;
    }

    @Bean
    public JsmBuildStatusListener jsmBuildStatusListener() {
        return new JsmBuildStatusListener();
    }

}
