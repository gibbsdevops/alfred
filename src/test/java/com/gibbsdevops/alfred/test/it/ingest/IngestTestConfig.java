package com.gibbsdevops.alfred.test.it.ingest;

import com.gibbsdevops.alfred.cache.AlfredCache;
import com.gibbsdevops.alfred.service.build.BuildService;
import com.gibbsdevops.alfred.service.job.JobService;
import com.gibbsdevops.alfred.web.controller.IngestApiController;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import static org.mockito.Mockito.mock;

@Configuration
@ComponentScan("com.gibbsdevops.alfred.service.ingest,com.gibbsdevops.alfred.repository")
@EnableJpaRepositories("com.gibbsdevops.alfred.dao")
@EnableTransactionManagement
@EnableCaching
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
    public BuildService buildService() {
        return mock(BuildService.class);
    }

    @Bean
    public SimpMessagingTemplate simpleMessagingTemplate() {
        return mock(SimpMessagingTemplate.class);
    }

    @Bean
    public CacheManager cacheManager() {
        Set<Cache> caches = new HashSet<>();
        caches.add(new AlfredCache("AlfredGitUser"));

        SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
        simpleCacheManager.setCaches(caches);
        simpleCacheManager.afterPropertiesSet();
        return simpleCacheManager;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory());
        return txManager;
    }

    @Bean
    public EntityManagerFactory entityManagerFactory() {
        Properties props = new Properties();
        props.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        // props.setProperty("hibernate.show_sql", "true");
        props.setProperty("hibernate.format_sql", "true");
        // props.setProperty("hibernate.hbm2ddl.auto", "validate");
        props.setProperty("hibernate.hbm2ddl.auto", "");
        props.setProperty("hibernate.ejb.naming_strategy", "com.gibbsdevops.alfred.dao.AlfredNamingStrategy");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("com.gibbsdevops.alfred.model");
        factory.setDataSource(dataSource());
        factory.setJpaProperties(props);
        factory.afterPropertiesSet();

        return factory.getObject();
    }

    @Bean
    public DataSource dataSource() {
        BasicDataSource dbcp = new BasicDataSource();
        dbcp.setDriverClassName("org.h2.Driver");
        dbcp.setUrl("jdbc:h2:./target/test-db;MODE=PostgreSQL;TRACE_LEVEL_FILE=4");
        dbcp.setUsername("");
        dbcp.setPassword("");
        dbcp.setMaxActive(5);
        dbcp.setMaxIdle(2);
        dbcp.setInitialSize(2);
        dbcp.setValidationQuery("SELECT 1");
        return dbcp;
    }

}
