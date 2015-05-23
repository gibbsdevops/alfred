package com.gibbsdevops.alfred.config;

import com.gibbsdevops.alfred.web.RequestLogger;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@Import({MvcConfig.class, DatabaseConfig.class, SchedulingConfig.class, CacheConfig.class})
@ComponentScan("com.gibbsdevops.alfred")
@EnableJpaRepositories("com.gibbsdevops.alfred.dao")
public class AppConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RequestLogger()).addPathPatterns("/**");
    }

}
