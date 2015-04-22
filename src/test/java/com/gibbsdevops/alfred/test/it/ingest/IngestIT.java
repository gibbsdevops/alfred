package com.gibbsdevops.alfred.test.it.ingest;

import com.gibbsdevops.alfred.config.MvcConfig;
import com.gibbsdevops.alfred.dao.AlfredGitUserDao;
import com.gibbsdevops.alfred.model.alfred.AlfredCommitNode;
import com.gibbsdevops.alfred.model.alfred.AlfredJobNode;
import com.gibbsdevops.alfred.model.alfred.AlfredRepoNode;
import com.gibbsdevops.alfred.model.alfred.AlfredUser;
import com.gibbsdevops.alfred.repository.AlfredRepository;
import com.gibbsdevops.alfred.service.build.BuildService;
import com.gibbsdevops.alfred.web.controller.IngestApiController;
import org.apache.commons.io.IOUtils;
import org.flywaydb.core.Flyway;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
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
import static org.junit.Assert.*;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
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
    private BuildService buildService;

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

        reset(buildService);

        LOG.info("Clearing cache");
        cacheManager.getCacheNames().stream().forEach(c -> cacheManager.getCache(c).clear());

        LOG.info("Setting up new database");
        Connection connection = dataSource.getConnection();
        connection.createStatement().execute("drop all objects;");
        connection.close();

        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource);
        flyway.migrate();

    }

    @Test
    public void testIngestPushToOrgRepo() throws Exception {
        mockMvc.perform(post("/ingest")
                .contentType(APPLICATION_JSON_UTF8)
                .content(IOUtils.toString(getClass().getResource("push-to-org-repo.json").openStream()))
                .accept(APPLICATION_JSON_UTF8)
                .header("X-Github-Delivery", "abc")
                .header("X-Github-Event", "push"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8));

        assertThat(stringifyRowQuery("select * from alfred_git_user where name='shanegibbs'"),
                equalTo("NAME=shanegibbs\nEMAIL=shane@hands.net.nz\n"));
        assertThat(stringifyRowQuery("select * from alfred_git_user where name='Shane Gibbs'"),
                equalTo("NAME=Shane Gibbs\nEMAIL=shane@hands.net.nz\n"));
        assertThat(stringifyRowQuery("select count(*) as count from alfred_git_user"),
                equalTo("COUNT=2\n"));

        assertThat(stringifyRowQuery("select * from alfred_user where login='shanegibbs'"), equalTo("LOGIN=shanegibbs\n" +
                "GITHUB_ID=2838876\n" +
                "NAME=Shane Gibbs\n" +
                "EMAIL=\n" +
                "URL=https://api.github.com/users/shanegibbs\n" +
                "HTML_URL=https://github.com/shanegibbs\n" +
                "AVATAR_URL=https://avatars.githubusercontent.com/u/2838876?v=3\n" +
                "TYPE=User\n" +
                "DESCRIPTION=null\n" +
                "CREATED_AT=null\n" +
                "UPDATED_AT=null\n"));
        assertThat(stringifyRowQuery("select * from alfred_user where login='gibbsdevops'"), equalTo("LOGIN=gibbsdevops\n" +
                "GITHUB_ID=10710439\n" +
                "NAME=null\n" +
                "EMAIL=null\n" +
                "URL=https://api.github.com/orgs/gibbsdevops\n" +
                "HTML_URL=https://github.com/gibbsdevops\n" +
                "AVATAR_URL=https://avatars.githubusercontent.com/u/10710439?v=3\n" +
                "TYPE=Organization\n" +
                "DESCRIPTION=null\n" +
                "CREATED_AT=null\n" +
                "UPDATED_AT=null\n"));
        assertThat(stringifyRowQuery("select count(*) as count from alfred_user"),
                equalTo("COUNT=2\n"));

        assertThat(stringifyRowQuery("select * from alfred_repo where name = 'alfred'"), equalTo("GITHUB_ID=30513996\n" +
                "NAME=alfred\n" +
                "FULL_NAME=gibbsdevops/alfred\n" +
                "PRIVATE=FALSE\n" +
                "DESCRIPTION=Alfred CI Server\n" +
                "FORK=FALSE\n" +
                "URL=https://api.github.com/repos/gibbsdevops/alfred\n" +
                "HTML_URL=https://github.com/gibbsdevops/alfred\n" +
                "SSH_URL=git@github.com:gibbsdevops/alfred.git\n" +
                "GIT_URL=git://github.com/gibbsdevops/alfred.git\n" +
                "CLONE_URL=https://github.com/gibbsdevops/alfred.git\n" +
                "CREATED_AT=1423447114\n" +
                "UPDATED_AT=1428459143\n" +
                "PUSHED_AT=1429455933\n" +
                "HOMEPAGE=\n" +
                "LANGUAGE=Java\n" +
                "DEFAULT_BRANCH=master\n" +
                "OWNER=1\n"));
        assertThat(stringifyRowQuery("select count(*) as count from alfred_repo"),
                equalTo("COUNT=1\n"));

        assertThat(stringifyRowQuery("select * from alfred_commit"), equalTo("REPO=1\n" +
                "COMMITTER=2\n" +
                "AUTHOR=2\n" +
                "PUSHER=1\n" +
                "SENDER=2\n" +
                "HASH=ea77d7ca8ef5d31ad9b2ac6bd1cd0376656a5fe3\n" +
                "MESSAGE=Add org and repo pages\n" +
                "TIMESTAMP=1427055873\n" +
                "ADDITIONS=0\n" +
                "DELETIONS=0\n"));
        assertThat(stringifyRowQuery("select count(*) as count from alfred_commit"),
                equalTo("COUNT=1\n"));

        assertThat(stringifyRowQuery("select * from alfred_job"), equalTo("COMMIT=1\n" +
                "STATUS=queued\n" +
                "ERROR=null\n"));
        assertThat(stringifyRowQuery("select count(*) as count from alfred_job"),
                equalTo("COUNT=1\n"));

        ArgumentCaptor<AlfredJobNode> argument = ArgumentCaptor.forClass(AlfredJobNode.class);
        verify(buildService).submit(argument.capture());

        AlfredJobNode job = argument.getValue();
        assertNotNull(job);
        assertEquals("queued", job.getStatus());
        assertNull(job.getError());

        AlfredCommitNode commit = job.getCommit();
        assertNotNull(commit);
        assertEquals("ea77d7ca8ef5d31ad9b2ac6bd1cd0376656a5fe3", commit.getHash());

        AlfredRepoNode repo = commit.getRepo();
        assertNotNull(repo);
        assertEquals("https://github.com/gibbsdevops/alfred.git", repo.getCloneUrl());
        assertEquals("alfred", repo.getName());

        AlfredUser owner = repo.getOwner();
        assertNotNull(owner);
        assertEquals("gibbsdevops", owner.getLogin());
    }

    @Test
    public void testIngestPushToUserRepo() throws Exception {
        mockMvc.perform(post("/ingest")
                .contentType(MediaType.APPLICATION_JSON)
                .content(IOUtils.toString(getClass().getResource("push-to-user-repo.json").openStream()))
                .accept(MediaType.APPLICATION_JSON)
                .header("X-Github-Delivery", "abc")
                .header("X-Github-Event", "push"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        assertThat(stringifyRowQuery("select * from alfred_git_user where name='shanegibbs'"),
                equalTo("NAME=shanegibbs\nEMAIL=shane@hands.net.nz\n"));
        assertThat(stringifyRowQuery("select * from alfred_git_user where name='Shane Gibbs'"),
                equalTo("NAME=Shane Gibbs\nEMAIL=shane@hands.net.nz\n"));
        assertThat(stringifyRowQuery("select count(*) as count from alfred_git_user"),
                equalTo("COUNT=2\n"));

        assertThat(stringifyRowQuery("select * from alfred_user where login='shanegibbs'"), equalTo("LOGIN=shanegibbs\n" +
                "GITHUB_ID=2838876\n" +
                "NAME=Shane Gibbs\n" +
                "EMAIL=\n" +
                "URL=https://api.github.com/users/shanegibbs\n" +
                "HTML_URL=https://github.com/shanegibbs\n" +
                "AVATAR_URL=https://avatars.githubusercontent.com/u/2838876?v=3\n" +
                "TYPE=User\n" +
                "DESCRIPTION=null\n" +
                "CREATED_AT=null\n" +
                "UPDATED_AT=null\n"));
        assertThat(stringifyRowQuery("select count(*) as count from alfred_user"),
                equalTo("COUNT=1\n"));

        assertThat(stringifyRowQuery("select * from alfred_repo"), equalTo("GITHUB_ID=30393711\n" +
                "NAME=alfred-test-repo\n" +
                "FULL_NAME=shanegibbs/alfred-test-repo\n" +
                "PRIVATE=FALSE\n" +
                "DESCRIPTION=\n" +
                "FORK=FALSE\n" +
                "URL=https://api.github.com/repos/shanegibbs/alfred-test-repo\n" +
                "HTML_URL=https://github.com/shanegibbs/alfred-test-repo\n" +
                "SSH_URL=git@github.com:shanegibbs/alfred-test-repo.git\n" +
                "GIT_URL=git://github.com/shanegibbs/alfred-test-repo.git\n" +
                "CLONE_URL=https://github.com/shanegibbs/alfred-test-repo.git\n" +
                "CREATED_AT=1423194200\n" +
                "UPDATED_AT=1423194200\n" +
                "PUSHED_AT=1424659550\n" +
                "HOMEPAGE=null\n" +
                "LANGUAGE=null\n" +
                "DEFAULT_BRANCH=master\n" +
                "OWNER=1\n"));
        assertThat(stringifyRowQuery("select count(*) as count from alfred_repo"),
                equalTo("COUNT=1\n"));

        assertThat(stringifyRowQuery("select * from alfred_commit"), equalTo("REPO=1\n" +
                "COMMITTER=2\n" +
                "AUTHOR=2\n" +
                "PUSHER=1\n" +
                "SENDER=1\n" +
                "HASH=49dc5f48b404770698bc5ecca0c9a11485b32915\n" +
                "MESSAGE=Update README.md\n" +
                "TIMESTAMP=1423194259\n" +
                "ADDITIONS=0\n" +
                "DELETIONS=0\n"));
        assertThat(stringifyRowQuery("select count(*) as count from alfred_commit"),
                equalTo("COUNT=1\n"));

        assertThat(stringifyRowQuery("select * from alfred_job"), equalTo("COMMIT=1\n" +
                "STATUS=queued\n" +
                "ERROR=null\n"));
        assertThat(stringifyRowQuery("select count(*) as count from alfred_job"),
                equalTo("COUNT=1\n"));

        ArgumentCaptor<AlfredJobNode> argument = ArgumentCaptor.forClass(AlfredJobNode.class);
        verify(buildService).submit(argument.capture());

        AlfredJobNode job = argument.getValue();
        assertNotNull(job);
        assertEquals("queued", job.getStatus());
        assertNull(job.getError());

        AlfredCommitNode commit = job.getCommit();
        assertNotNull(commit);
        assertEquals("49dc5f48b404770698bc5ecca0c9a11485b32915", commit.getHash());

        AlfredRepoNode repo = commit.getRepo();
        assertNotNull(repo);
        assertEquals("https://github.com/shanegibbs/alfred-test-repo.git", repo.getCloneUrl());
        assertEquals("alfred-test-repo", repo.getName());

        AlfredUser owner = repo.getOwner();
        assertNotNull(owner);
        assertEquals("shanegibbs", owner.getLogin());
    }

    String stringifyRowQuery(String sql) {
        ResultSet rs = null;
        Connection connection = null;

        try {
            connection = dataSource.getConnection();
            rs = connection.createStatement().executeQuery(sql);
            if (!rs.next()) throw new RuntimeException("No record found for query: " + sql);

            StringBuilder sb = new StringBuilder("");
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                if ("id".equals(rs.getMetaData().getColumnName(i).toLowerCase())) continue;
                if ("version".equals(rs.getMetaData().getColumnName(i).toLowerCase())) continue;
                sb.append(rs.getMetaData().getColumnName(i)).append("=");
                sb.append(rs.getString(i)).append("\n");
            }

            assertFalse("Expecting single row but there is >1", rs.next());

            return sb.toString();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to stringify ResultSet", e);
        } finally {
            assert connection != null;
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            assert connection != null;
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
