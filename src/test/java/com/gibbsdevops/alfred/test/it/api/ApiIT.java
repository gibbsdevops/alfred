package com.gibbsdevops.alfred.test.it.api;

import com.gibbsdevops.alfred.config.MvcConfig;
import org.flywaydb.core.Flyway;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import javax.sql.DataSource;
import java.nio.charset.Charset;
import java.sql.Connection;

import static org.mockito.Mockito.reset;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {MvcConfig.class, ApiTestConfig.class})
public class ApiIT {

    private static final Logger LOG = LoggerFactory.getLogger(ApiIT.class);

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private CacheManager cacheManager;

    private MockMvc mockMvc;

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(
            MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    @Before
    public void setup() throws Exception {
        mockMvc = webAppContextSetup(webApplicationContext).build();

        LOG.info("Clearing cache");
        cacheManager.getCacheNames().stream().forEach(c -> cacheManager.getCache(c).clear());

        LOG.info("Setting up new database");
        Connection connection = dataSource.getConnection();
        connection.createStatement().execute("drop owned by alfred_test cascade;");
        connection.close();

        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource);
        flyway.migrate();

    }

    @Test
    public void testSomething() {
        
    }

}
