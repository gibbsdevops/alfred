package com.gibbsdevops.alfred.test.it.ingest;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class TestDatabaseConfig {

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
