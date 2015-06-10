package com.gibbsdevops.alfred.test.it.ingest;

import com.gibbsdevops.alfred.config.web.MvcConfig;
import com.gibbsdevops.alfred.dao.AlfredGitUserDao;
import com.gibbsdevops.alfred.model.alfred.AlfredCommitNode;
import com.gibbsdevops.alfred.model.alfred.AlfredJobNode;
import com.gibbsdevops.alfred.model.alfred.AlfredRepoNode;
import com.gibbsdevops.alfred.model.alfred.AlfredUser;
import com.gibbsdevops.alfred.repository.AlfredRepository;
import com.gibbsdevops.alfred.service.build.BuildQueueSubmitter;
import com.gibbsdevops.alfred.utils.rest.DateTimeUtils;
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
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

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
    private BuildQueueSubmitter buildQueue;

    @Autowired
    private CacheManager cacheManager;

    private MockMvc mockMvc;

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(
            MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    @Before
    public void setup() throws Exception {
        Instant instant = Instant.parse("2015-04-29T21:30:00Z");
        DateTimeUtils.setClock(Clock.fixed(instant, ZoneId.of("UTC")));

        mockMvc = webAppContextSetup(webApplicationContext).build();

        reset(buildQueue);

        LOG.info("Clearing cache");
        cacheManager.getCacheNames().stream().forEach(c -> cacheManager.getCache(c).clear());

        LOG.info("Setting up new database");
        Connection connection = dataSource.getConnection();
        connection.createStatement().execute(String.format("drop owned by %s cascade;",
                System.getenv().getOrDefault("ALFRED_TEST_DB_USERNAME", "alfred_test")));
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
                equalTo("name=shanegibbs\nemail=shane@hands.net.nz\n"));
        assertThat(stringifyRowQuery("select * from alfred_git_user where name='Shane Gibbs'"),
                equalTo("name=Shane Gibbs\nemail=shane@hands.net.nz\n"));
        assertThat(stringifyRowQuery("select count(*) as count from alfred_git_user"),
                equalTo("count=2\n"));

        assertThat(stringifyRowQuery("select * from alfred_user where login='shanegibbs'"), equalTo("login=shanegibbs\n" +
                "github_id=2838876\n" +
                "name=Shane Gibbs\n" +
                "email=\n" +
                "url=https://api.github.com/users/shanegibbs\n" +
                "html_url=https://github.com/shanegibbs\n" +
                "avatar_url=https://avatars.githubusercontent.com/u/2838876?v=3\n" +
                "type=User\n" +
                "description=null\n" +
                "github_created_at=null\n" +
                "github_updated_at=null\n"));
        assertThat(stringifyRowQuery("select * from alfred_user where login='gibbsdevops'"), equalTo("login=gibbsdevops\n" +
                "github_id=10710439\n" +
                "name=null\n" +
                "email=null\n" +
                "url=https://api.github.com/orgs/gibbsdevops\n" +
                "html_url=https://github.com/gibbsdevops\n" +
                "avatar_url=https://avatars.githubusercontent.com/u/10710439?v=3\n" +
                "type=Organization\n" +
                "description=null\n" +
                "github_created_at=null\n" +
                "github_updated_at=null\n"));
        assertThat(stringifyRowQuery("select count(*) as count from alfred_user"),
                equalTo("count=2\n"));

        assertThat(stringifyRowQuery("select * from alfred_repo where name = 'alfred'"), equalTo("owner_id=1\n" +
                "github_id=30513996\n" +
                "name=alfred\n" +
                "full_name=gibbsdevops/alfred\n" +
                "private=f\n" +
                "description=Alfred CI Server\n" +
                "fork=f\n" +
                "url=https://api.github.com/repos/gibbsdevops/alfred\n" +
                "html_url=https://github.com/gibbsdevops/alfred\n" +
                "ssh_url=git@github.com:gibbsdevops/alfred.git\n" +
                "git_url=git://github.com/gibbsdevops/alfred.git\n" +
                "clone_url=https://github.com/gibbsdevops/alfred.git\n" +
                "github_created_at=1423447114\n" +
                "github_updated_at=1428459143\n" +
                "pushed_at=1429455933\n" +
                "homepage=\n" +
                "language=Java\n" +
                "default_branch=master\n"));
        assertThat(stringifyRowQuery("select count(*) as count from alfred_repo"),
                equalTo("count=1\n"));

        assertThat(stringifyRowQuery("select * from alfred_commit"), equalTo("repo_id=1\n" +
                "committer_id=2\n" +
                "author_id=2\n" +
                "pusher_id=1\n" +
                "sender_id=2\n" +
                "hash=ea77d7ca8ef5d31ad9b2ac6bd1cd0376656a5fe3\n" +
                "message=Add org and repo pages\n" +
                "timestamp=1427055873\n" +
                "additions=0\n" +
                "deletions=0\n"));
        assertThat(stringifyRowQuery("select count(*) as count from alfred_commit"),
                equalTo("count=1\n"));

        assertThat(stringifyRowQuery("select * from alfred_job"), equalTo("commit_id=1\n" +
                "status=queued\n" +
                "error=null\n" +
                "duration=null\n" +
                "created_at=1430343000\n"));
        assertThat(stringifyRowQuery("select count(*) as count from alfred_job"),
                equalTo("count=1\n"));

        ArgumentCaptor<AlfredJobNode> argument = ArgumentCaptor.forClass(AlfredJobNode.class);
        verify(buildQueue).submit(argument.capture());

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
                equalTo("name=shanegibbs\nemail=shane@hands.net.nz\n"));
        assertThat(stringifyRowQuery("select * from alfred_git_user where name='Shane Gibbs'"),
                equalTo("name=Shane Gibbs\nemail=shane@hands.net.nz\n"));
        assertThat(stringifyRowQuery("select count(*) as count from alfred_git_user"),
                equalTo("count=2\n"));

        assertThat(stringifyRowQuery("select * from alfred_user where login='shanegibbs'"), equalTo("login=shanegibbs\n" +
                "github_id=2838876\n" +
                "name=Shane Gibbs\n" +
                "email=\n" +
                "url=https://api.github.com/users/shanegibbs\n" +
                "html_url=https://github.com/shanegibbs\n" +
                "avatar_url=https://avatars.githubusercontent.com/u/2838876?v=3\n" +
                "type=User\n" +
                "description=null\n" +
                "github_created_at=null\n" +
                "github_updated_at=null\n"));
        assertThat(stringifyRowQuery("select count(*) as count from alfred_user"),
                equalTo("count=1\n"));

        assertThat(stringifyRowQuery("select * from alfred_repo"), equalTo("owner_id=1\n" +
                "github_id=30393711\n" +
                "name=alfred-test-repo\n" +
                "full_name=shanegibbs/alfred-test-repo\n" +
                "private=f\n" +
                "description=\n" +
                "fork=f\n" +
                "url=https://api.github.com/repos/shanegibbs/alfred-test-repo\n" +
                "html_url=https://github.com/shanegibbs/alfred-test-repo\n" +
                "ssh_url=git@github.com:shanegibbs/alfred-test-repo.git\n" +
                "git_url=git://github.com/shanegibbs/alfred-test-repo.git\n" +
                "clone_url=https://github.com/shanegibbs/alfred-test-repo.git\n" +
                "github_created_at=1423194200\n" +
                "github_updated_at=1423194200\n" +
                "pushed_at=1424659550\n" +
                "homepage=null\n" +
                "language=null\n" +
                "default_branch=master\n"));
        assertThat(stringifyRowQuery("select count(*) as count from alfred_repo"),
                equalTo("count=1\n"));

        assertThat(stringifyRowQuery("select * from alfred_commit"), equalTo("repo_id=1\n" +
                "committer_id=2\n" +
                "author_id=2\n" +
                "pusher_id=1\n" +
                "sender_id=1\n" +
                "hash=49dc5f48b404770698bc5ecca0c9a11485b32915\n" +
                "message=Update README.md\n" +
                "timestamp=1423194259\n" +
                "additions=0\n" +
                "deletions=0\n"));
        assertThat(stringifyRowQuery("select count(*) as count from alfred_commit"),
                equalTo("count=1\n"));

        assertThat(stringifyRowQuery("select * from alfred_job"), equalTo("commit_id=1\n" +
                "status=queued\n" +
                "error=null\n" +
                "duration=null\n" +
                "created_at=1430343000\n"));
        assertThat(stringifyRowQuery("select count(*) as count from alfred_job"),
                equalTo("count=1\n"));

        ArgumentCaptor<AlfredJobNode> argument = ArgumentCaptor.forClass(AlfredJobNode.class);
        verify(buildQueue).submit(argument.capture());

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

    @Test
    public void testIngestTwice() throws Exception {

        mockMvc.perform(post("/ingest")
                .contentType(APPLICATION_JSON_UTF8)
                .content(IOUtils.toString(getClass().getResource("push-to-org-repo.json").openStream()))
                .accept(APPLICATION_JSON_UTF8)
                .header("X-Github-Delivery", "abc")
                .header("X-Github-Event", "push"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8));

        mockMvc.perform(post("/ingest")
                .contentType(APPLICATION_JSON_UTF8)
                .content(IOUtils.toString(getClass().getResource("push-to-org-repo.json").openStream()))
                .accept(APPLICATION_JSON_UTF8)
                .header("X-Github-Delivery", "abc")
                .header("X-Github-Event", "push"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8));

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
