package com.gibbsdevops.alfred.test.it;

import com.gibbsdevops.alfred.service.build.BuildService;
import com.gibbsdevops.alfred.service.job.JobService;
import com.gibbsdevops.alfred.web.controller.IngestApiController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Configuration
@ComponentScan("com.gibbsdevops.alfred.service.ingest")
public class IngestTestConfig {

    @Bean
    public IngestApiController api() {
        return new IngestApiController();
    }

    @Bean
    public JobService jobService() {
        return null;
    }

    @Bean
    public BuildService buildService() {
        return null;
    }

    @Bean
    public SimpMessagingTemplate simpMessagingTemplate() {
        return null;
    }

}
