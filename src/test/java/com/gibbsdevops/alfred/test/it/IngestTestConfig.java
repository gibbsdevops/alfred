package com.gibbsdevops.alfred.test.it;

import com.gibbsdevops.alfred.repository.AlfredUserRepository;
import com.gibbsdevops.alfred.service.build.BuildService;
import com.gibbsdevops.alfred.service.job.JobService;
import com.gibbsdevops.alfred.web.controller.IngestApiController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import static org.mockito.Mockito.mock;

@Configuration
@ComponentScan("com.gibbsdevops.alfred.service.ingest")
public class IngestTestConfig {

    @Bean
    public IngestApiController ingestApiController() {
        return new IngestApiController();
    }

    @Bean
    public JobService jobService() {
        return mock(JobService.class);
    }

    @Bean
    public AlfredUserRepository alfredUserRepository() {
        return mock(AlfredUserRepository.class);
    }

    @Bean
    public BuildService buildService() {
        return mock(BuildService.class);
    }

    @Bean
    public SimpMessagingTemplate simpleMessagingTemplate() {
        return mock(SimpMessagingTemplate.class);
    }

}
