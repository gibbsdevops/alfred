package com.gibbsdevops.alfred.config;

import com.gibbsdevops.alfred.service.job.repositories.JobRepository;
import com.gibbsdevops.alfred.service.job.repositories.file.FileJobRepository;
import com.gibbsdevops.alfred.web.RequestLogger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@Import({MvcConfig.class, DatabaseConfig.class, CacheConfig.class})
@EnableWebMvc
@ComponentScan("com.gibbsdevops.alfred")
@EnableJpaRepositories("com.gibbsdevops.alfred.dao")
public class AppConfig extends WebMvcConfigurerAdapter {

    @Bean
    public ExecutorService buildExecutor() {
        ExecutorService executor = Executors.newFixedThreadPool(4);
        return executor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RequestLogger()).addPathPatterns("/**");
    }

}
