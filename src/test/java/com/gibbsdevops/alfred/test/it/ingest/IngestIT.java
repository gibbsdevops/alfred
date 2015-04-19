package com.gibbsdevops.alfred.test.it.ingest;

import com.gibbsdevops.alfred.config.MvcConfig;
import com.gibbsdevops.alfred.dao.AlfredGitUserDao;
import com.gibbsdevops.alfred.repository.AlfredRepository;
import com.gibbsdevops.alfred.web.controller.IngestApiController;
import org.apache.commons.io.IOUtils;
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
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@ContextConfiguration(classes = {MvcConfig.class, IngestTestConfig.class})
public class IngestIT {

    private static final Logger LOG = LoggerFactory.getLogger(IngestIT.class);

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private IngestApiController controller;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private AlfredGitUserDao alfredGitUserDao;

    @Autowired
    private AlfredRepository alfredRepository;

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

        Connection connection = dataSource.getConnection();
        connection.createStatement().execute("drop all objects;");
        connection.close();

        LOG.info("Setting up database");
        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource);
        flyway.migrate();

    }

    @Test
    public void testStatus() throws Exception {
        mockMvc.perform(get("/ingest").accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8));
    }

    @Test
    public void testIngest() throws Exception {
        mockMvc.perform(post("/ingest")
                .contentType(MediaType.APPLICATION_JSON)
                .content(IOUtils.toString(getClass().getResource("push-to-org-repo.json").openStream()))
                .accept(MediaType.APPLICATION_JSON)
                .header("X-Github-Delivery", "abc")
                .header("X-Github-Event", "push"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        Connection connection = dataSource.getConnection();
        ResultSet resultSet;

        resultSet = connection.createStatement().executeQuery("select * from alfred_git_user;");
        assertThat(stringize(resultSet, " "), equalTo("ID=1 NAME=shanegibbs EMAIL=shane@hands.net.nz "));
        assertThat(stringize(resultSet, " "), equalTo("ID=2 NAME=Shane Gibbs EMAIL=shane@hands.net.nz "));
        resultSet.close();

        resultSet = connection.createStatement().executeQuery("select * from alfred_user;");
        assertThat(stringize(resultSet), equalTo("LOGIN=shanegibbs\n" +
                "VERSION=0\n" +
                "ID=2838876\n" +
                "NAME=null\n" +
                "EMAIL=null\n" +
                "URL=https://api.github.com/users/shanegibbs\n" +
                "HTML_URL=https://github.com/shanegibbs\n" +
                "AVATAR_URL=https://avatars.githubusercontent.com/u/2838876?v=3\n" +
                "TYPE=User\n" +
                "DESCRIPTION=null\n" +
                "CREATED_AT=null\n" +
                "UPDATED_AT=null\n"));
        assertThat(stringize(resultSet), equalTo("LOGIN=gibbsdevops\n" +
                "VERSION=0\n" +
                "ID=10710439\n" +
                "NAME=null\n" +
                "EMAIL=null\n" +
                "URL=https://api.github.com/orgs/gibbsdevops\n" +
                "HTML_URL=null\n" +
                "AVATAR_URL=https://avatars.githubusercontent.com/u/10710439?v=3\n" +
                "TYPE=Organization\n" +
                "DESCRIPTION=null\n" +
                "CREATED_AT=null\n" +
                "UPDATED_AT=null\n"));
        resultSet.close();

        connection.close();
    }

    String stringize(ResultSet rs) {
        return stringize(rs, "\n");
    }

    String stringize(ResultSet rs, String sep) {
        try {
            rs.next();
            StringBuilder sb = new StringBuilder("");
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                sb.append(rs.getMetaData().getColumnName(i)).append("=");
                sb.append(rs.getString(i)).append(sep);
            }
            return sb.toString();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to stringize ResultSet", e);
        }
    }

}
