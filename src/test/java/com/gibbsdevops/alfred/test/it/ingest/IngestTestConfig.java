package com.gibbsdevops.alfred.test.it.ingest;

import com.gibbsdevops.alfred.config.web.CacheConfig;
import com.gibbsdevops.alfred.config.web.MvcConfig;
import com.gibbsdevops.alfred.service.build.BuildQueueSubmitter;
import com.gibbsdevops.alfred.service.build.BuildStatusService;
import com.gibbsdevops.alfred.test.it.TestDatabaseConfig;
import com.gibbsdevops.alfred.utils.rest.JsonRestClient;
import com.gibbsdevops.alfred.utils.rest.MockJsonRestClient;
import com.gibbsdevops.alfred.web.controller.IngestApiController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.io.IOException;

import static org.mockito.Mockito.mock;

@Configuration
@Import({TestDatabaseConfig.class, MvcConfig.class, CacheConfig.class})
@ComponentScan("com.gibbsdevops.alfred.service.ingest,com.gibbsdevops.alfred.repository,com.gibbsdevops.alfred.service.github")
@EnableJpaRepositories("com.gibbsdevops.alfred.dao")
public class IngestTestConfig {

    @Bean
    public IngestApiController ingestApiController() {
        return new IngestApiController();
    }

    @Bean
    public JsonRestClient jsonRestClient() throws IOException {
        MockJsonRestClient.Builder builder = new MockJsonRestClient.Builder(getClass());
        builder.get("https://api.github.com/orgs/gibbsdevops","github-responses/GET-orgs-gibbsdevops.json");
        builder.get("https://api.github.com/users/shanegibbs", "github-responses/GET-users-shanegibbs.json");
        builder.get("https://api.github.com/repos/gibbsdevops/alfred", "github-responses/GET-repos-gibbsdevops-alfred.json");
        builder.get("https://api.github.com/repos/shanegibbs/alfred-test-repo", "github-responses/GET-repos-shanegibbs-alfred-test-repo.json");
        return builder.getMockJsonRestClient();
    }

    @Bean
    public BuildQueueSubmitter buildQueue() {
        return mock(BuildQueueSubmitter.class);
    }

    @Bean
    public BuildStatusService buildService() {
        return mock(BuildStatusService.class);
    }

    @Bean
    public SimpMessagingTemplate simpleMessagingTemplate() {
        return mock(SimpMessagingTemplate.class);
    }

}
