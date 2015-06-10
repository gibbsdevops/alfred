package com.gibbsdevops.alfred.config.web;

import org.apache.commons.dbcp.BasicDataSource;
import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
public class DatabaseConfig {

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

        props.setProperty("hibernate.hbm2ddl.auto", "");
        if ("true".equals(System.getenv().getOrDefault("ALFRED_HIBERNATE_MIGRATE", "false"))) {
            props.setProperty("hibernate.hbm2ddl.auto", "create");
        }

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
        dbcp.setDriverClassName(System.getenv().getOrDefault("ALFRED_DB_DRIVER", "org.postgresql.Driver"));
        dbcp.setUrl(System.getenv().getOrDefault("ALFRED_DB_URL", "jdbc:postgresql://localhost/dev"));
        dbcp.setUsername(System.getenv().getOrDefault("ALFRED_DB_USERNAME", "alfred"));
        dbcp.setPassword(System.getenv().getOrDefault("ALFRED_DB_PASSWORD", "alfred"));
        dbcp.setMaxActive(5);
        dbcp.setMaxIdle(2);
        dbcp.setInitialSize(2);
        dbcp.setValidationQuery("SELECT 1");

        if ("true".equals(System.getenv().getOrDefault("ALFRED_DB_MIGRATE", "false"))) {
            Flyway flyway = new Flyway();
            flyway.setDataSource(dbcp);
            flyway.migrate();
        }

        return dbcp;
    }

}
