package com.gibbsdevops.alfred.config.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import javax.annotation.PostConstruct;

@Configuration
@EnableScheduling
public class SchedulingConfig implements SchedulingConfigurer {

    private ThreadPoolTaskScheduler taskScheduler;

    @PostConstruct
    public void setup() {
        taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.initialize();
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskScheduler);
    }

}
