package com.gibbsdevops.alfred.config.worker;

import com.gibbsdevops.alfred.config.common.JmsConfig;
import com.gibbsdevops.alfred.service.build.BuildStatusService;
import com.gibbsdevops.alfred.service.build.worker.JmsBuildStatusSender;
import com.gibbsdevops.alfred.service.build.worker.JmsJobBuildListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;

import javax.jms.ConnectionFactory;

@Configuration
@Import(JmsConfig.class)
public class WorkerConfig {

    @Autowired
    private ConnectionFactory connectionFactory;

    @Bean
    public DefaultJmsListenerContainerFactory jobExecutorContainerFactory() {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setConcurrency("2");
        return factory;
    }

    @Bean
    public JmsJobBuildListener jmsJobExecutor() {
        return new JmsJobBuildListener();
    }

    @Bean
    public BuildStatusService buildStatusService() {
        return new JmsBuildStatusSender();
    }

}
