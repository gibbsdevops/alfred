package com.gibbsdevops.alfred.test.it.api;

import com.gibbsdevops.alfred.config.web.CacheConfig;
import com.gibbsdevops.alfred.config.web.MvcConfig;
import com.gibbsdevops.alfred.test.it.TestDatabaseConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@Import({TestDatabaseConfig.class, MvcConfig.class, CacheConfig.class})
@ComponentScan("com.gibbsdevops.alfred.repository")
@EnableJpaRepositories("com.gibbsdevops.alfred.dao")
public class ApiTestConfig {


}
