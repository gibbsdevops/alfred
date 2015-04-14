package com.gibbsdevops.alfred.test.it;

import com.gibbsdevops.alfred.repository.AlfredRepository;
import com.gibbsdevops.alfred.service.build.BuildService;
import com.gibbsdevops.alfred.service.job.JobService;
import com.gibbsdevops.alfred.web.controller.IngestApiController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;

import javax.sql.DataSource;

import java.util.Properties;

import static org.mockito.Mockito.mock;

@Configuration
@ComponentScan("com.gibbsdevops.alfred.service.ingest,com.gibbsdevops.alfred.repository")
public class IngestTestConfig {

    @Bean
    public IngestApiController ingestApiController() {
        return new IngestApiController();
    }

    @Bean
    public JobService jobService() {
        return mock(JobService.class);
    }

    /*
    @Bean
    public AlfredRepository alfredRepository() {
        return mock(AlfredRepository.class);
    }
    */

    @Bean
    public BuildService buildService() {
        return mock(BuildService.class);
    }

    @Bean
    public SimpMessagingTemplate simpleMessagingTemplate() {
        return mock(SimpMessagingTemplate.class);
    }

    @Bean
    public DataSource dataSource() throws ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        Properties props = new Properties();
        SQLiteConfig config = new SQLiteConfig(props);
        return new SQLiteDataSource(config);
    }

}
