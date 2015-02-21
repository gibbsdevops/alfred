package com.gibbsdevops.alfred.config;

import com.gibbsdevops.alfred.service.job.repositories.JobRepository;
import com.gibbsdevops.alfred.service.job.repositories.file.FileJobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@EnableWebMvc
@ComponentScan("com.gibbsdevops.alfred")
public class AppConfig {

    @Bean
    public ExecutorService buildExecutor() {
        ExecutorService executor = Executors.newFixedThreadPool(4);
        return executor;
    }

    @Bean
    public JobRepository jobRepository() {
        return new FileJobRepository(new File("jobs"));
    }

}
