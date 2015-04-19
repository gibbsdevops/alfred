package com.gibbsdevops.alfred.test.it.ingest;

import com.gibbsdevops.alfred.cache.AlfredCache;
import com.gibbsdevops.alfred.service.build.BuildService;
import com.gibbsdevops.alfred.service.github.DefaultGithubApiService;
import com.gibbsdevops.alfred.service.github.GithubApiService;
import com.gibbsdevops.alfred.service.job.JobService;
import com.gibbsdevops.alfred.utils.rest.DefaultJsonRestClient;
import com.gibbsdevops.alfred.utils.rest.JsonRestClient;
import com.gibbsdevops.alfred.utils.rest.MockJsonRestClient;
import com.gibbsdevops.alfred.utils.rest.RestRequest;
import com.gibbsdevops.alfred.web.RequestLogger;
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
    public GithubApiService githubApiService() {
        return new DefaultGithubApiService();
    }

    @Bean
    public JsonRestClient jsonRestClient() {
        MockJsonRestClient mock = new MockJsonRestClient();
        mock.addRequest(RestRequest.get("https://api.github.com/orgs/gibbsdevops"), "{\"login\":\"gibbsdevops\",\"id\":10710439,\"url\":\"https://api.github.com/orgs/gibbsdevops\",\"repos_url\":\"https://api.github.com/orgs/gibbsdevops/repos\",\"events_url\":\"https://api.github.com/orgs/gibbsdevops/events\",\"members_url\":\"https://api.github.com/orgs/gibbsdevops/members{/member}\",\"public_members_url\":\"https://api.github.com/orgs/gibbsdevops/public_members{/member}\",\"avatar_url\":\"https://avatars.githubusercontent.com/u/10710439?v=3\",\"description\":null,\"public_repos\":1,\"public_gists\":0,\"followers\":0,\"following\":0,\"html_url\":\"https://github.com/gibbsdevops\",\"created_at\":\"2015-01-26T15:18:10Z\",\"updated_at\":\"2015-04-08T02:12:23Z\",\"type\":\"Organization\"}\n");
        mock.addRequest(RestRequest.get("https://api.github.com/users/shanegibbs"), "{\"login\":\"shanegibbs\",\"id\":2838876,\"avatar_url\":\"https://avatars.githubusercontent.com/u/2838876?v=3\",\"gravatar_id\":\"\",\"url\":\"https://api.github.com/users/shanegibbs\",\"html_url\":\"https://github.com/shanegibbs\",\"followers_url\":\"https://api.github.com/users/shanegibbs/followers\",\"following_url\":\"https://api.github.com/users/shanegibbs/following{/other_user}\",\"gists_url\":\"https://api.github.com/users/shanegibbs/gists{/gist_id}\",\"starred_url\":\"https://api.github.com/users/shanegibbs/starred{/owner}{/repo}\",\"subscriptions_url\":\"https://api.github.com/users/shanegibbs/subscriptions\",\"organizations_url\":\"https://api.github.com/users/shanegibbs/orgs\",\"repos_url\":\"https://api.github.com/users/shanegibbs/repos\",\"events_url\":\"https://api.github.com/users/shanegibbs/events{/privacy}\",\"received_events_url\":\"https://api.github.com/users/shanegibbs/received_events\",\"type\":\"User\",\"site_admin\":false,\"name\":\"Shane Gibbs\",\"company\":\"\",\"blog\":\"\",\"location\":\"Chicago, IL\",\"email\":\"\",\"hireable\":true,\"bio\":null,\"public_repos\":11,\"public_gists\":3,\"followers\":1,\"following\":0,\"created_at\":\"2012-11-19T23:24:28Z\",\"updated_at\":\"2015-04-18T19:50:27Z\"}\n");
        mock.addRequest(RestRequest.get("https://api.github.com/repos/gibbsdevops/alfred"), "{\"id\":30513996,\"name\":\"alfred\",\"full_name\":\"gibbsdevops/alfred\",\"owner\":{\"login\":\"gibbsdevops\",\"id\":10710439,\"avatar_url\":\"https://avatars.githubusercontent.com/u/10710439?v=3\",\"gravatar_id\":\"\",\"url\":\"https://api.github.com/users/gibbsdevops\",\"html_url\":\"https://github.com/gibbsdevops\",\"followers_url\":\"https://api.github.com/users/gibbsdevops/followers\",\"following_url\":\"https://api.github.com/users/gibbsdevops/following{/other_user}\",\"gists_url\":\"https://api.github.com/users/gibbsdevops/gists{/gist_id}\",\"starred_url\":\"https://api.github.com/users/gibbsdevops/starred{/owner}{/repo}\",\"subscriptions_url\":\"https://api.github.com/users/gibbsdevops/subscriptions\",\"organizations_url\":\"https://api.github.com/users/gibbsdevops/orgs\",\"repos_url\":\"https://api.github.com/users/gibbsdevops/repos\",\"events_url\":\"https://api.github.com/users/gibbsdevops/events{/privacy}\",\"received_events_url\":\"https://api.github.com/users/gibbsdevops/received_events\",\"type\":\"Organization\",\"site_admin\":false},\"private\":false,\"html_url\":\"https://github.com/gibbsdevops/alfred\",\"description\":\"Alfred CI Server\",\"fork\":false,\"url\":\"https://api.github.com/repos/gibbsdevops/alfred\",\"forks_url\":\"https://api.github.com/repos/gibbsdevops/alfred/forks\",\"keys_url\":\"https://api.github.com/repos/gibbsdevops/alfred/keys{/key_id}\",\"collaborators_url\":\"https://api.github.com/repos/gibbsdevops/alfred/collaborators{/collaborator}\",\"teams_url\":\"https://api.github.com/repos/gibbsdevops/alfred/teams\",\"hooks_url\":\"https://api.github.com/repos/gibbsdevops/alfred/hooks\",\"issue_events_url\":\"https://api.github.com/repos/gibbsdevops/alfred/issues/events{/number}\",\"events_url\":\"https://api.github.com/repos/gibbsdevops/alfred/events\",\"assignees_url\":\"https://api.github.com/repos/gibbsdevops/alfred/assignees{/user}\",\"branches_url\":\"https://api.github.com/repos/gibbsdevops/alfred/branches{/branch}\",\"tags_url\":\"https://api.github.com/repos/gibbsdevops/alfred/tags\",\"blobs_url\":\"https://api.github.com/repos/gibbsdevops/alfred/git/blobs{/sha}\",\"git_tags_url\":\"https://api.github.com/repos/gibbsdevops/alfred/git/tags{/sha}\",\"git_refs_url\":\"https://api.github.com/repos/gibbsdevops/alfred/git/refs{/sha}\",\"trees_url\":\"https://api.github.com/repos/gibbsdevops/alfred/git/trees{/sha}\",\"statuses_url\":\"https://api.github.com/repos/gibbsdevops/alfred/statuses/{sha}\",\"languages_url\":\"https://api.github.com/repos/gibbsdevops/alfred/languages\",\"stargazers_url\":\"https://api.github.com/repos/gibbsdevops/alfred/stargazers\",\"contributors_url\":\"https://api.github.com/repos/gibbsdevops/alfred/contributors\",\"subscribers_url\":\"https://api.github.com/repos/gibbsdevops/alfred/subscribers\",\"subscription_url\":\"https://api.github.com/repos/gibbsdevops/alfred/subscription\",\"commits_url\":\"https://api.github.com/repos/gibbsdevops/alfred/commits{/sha}\",\"git_commits_url\":\"https://api.github.com/repos/gibbsdevops/alfred/git/commits{/sha}\",\"comments_url\":\"https://api.github.com/repos/gibbsdevops/alfred/comments{/number}\",\"issue_comment_url\":\"https://api.github.com/repos/gibbsdevops/alfred/issues/comments{/number}\",\"contents_url\":\"https://api.github.com/repos/gibbsdevops/alfred/contents/{+path}\",\"compare_url\":\"https://api.github.com/repos/gibbsdevops/alfred/compare/{base}...{head}\",\"merges_url\":\"https://api.github.com/repos/gibbsdevops/alfred/merges\",\"archive_url\":\"https://api.github.com/repos/gibbsdevops/alfred/{archive_format}{/ref}\",\"downloads_url\":\"https://api.github.com/repos/gibbsdevops/alfred/downloads\",\"issues_url\":\"https://api.github.com/repos/gibbsdevops/alfred/issues{/number}\",\"pulls_url\":\"https://api.github.com/repos/gibbsdevops/alfred/pulls{/number}\",\"milestones_url\":\"https://api.github.com/repos/gibbsdevops/alfred/milestones{/number}\",\"notifications_url\":\"https://api.github.com/repos/gibbsdevops/alfred/notifications{?since,all,participating}\",\"labels_url\":\"https://api.github.com/repos/gibbsdevops/alfred/labels{/name}\",\"releases_url\":\"https://api.github.com/repos/gibbsdevops/alfred/releases{/id}\",\"created_at\":\"2015-02-09T01:58:34Z\",\"updated_at\":\"2015-04-08T02:12:23Z\",\"pushed_at\":\"2015-04-19T15:05:33Z\",\"git_url\":\"git://github.com/gibbsdevops/alfred.git\",\"ssh_url\":\"git@github.com:gibbsdevops/alfred.git\",\"clone_url\":\"https://github.com/gibbsdevops/alfred.git\",\"svn_url\":\"https://github.com/gibbsdevops/alfred\",\"homepage\":\"\",\"size\":500,\"stargazers_count\":0,\"watchers_count\":0,\"language\":\"Java\",\"has_issues\":true,\"has_downloads\":true,\"has_wiki\":true,\"has_pages\":false,\"forks_count\":0,\"mirror_url\":null,\"open_issues_count\":0,\"forks\":0,\"open_issues\":0,\"watchers\":0,\"default_branch\":\"master\",\"organization\":{\"login\":\"gibbsdevops\",\"id\":10710439,\"avatar_url\":\"https://avatars.githubusercontent.com/u/10710439?v=3\",\"gravatar_id\":\"\",\"url\":\"https://api.github.com/users/gibbsdevops\",\"html_url\":\"https://github.com/gibbsdevops\",\"followers_url\":\"https://api.github.com/users/gibbsdevops/followers\",\"following_url\":\"https://api.github.com/users/gibbsdevops/following{/other_user}\",\"gists_url\":\"https://api.github.com/users/gibbsdevops/gists{/gist_id}\",\"starred_url\":\"https://api.github.com/users/gibbsdevops/starred{/owner}{/repo}\",\"subscriptions_url\":\"https://api.github.com/users/gibbsdevops/subscriptions\",\"organizations_url\":\"https://api.github.com/users/gibbsdevops/orgs\",\"repos_url\":\"https://api.github.com/users/gibbsdevops/repos\",\"events_url\":\"https://api.github.com/users/gibbsdevops/events{/privacy}\",\"received_events_url\":\"https://api.github.com/users/gibbsdevops/received_events\",\"type\":\"Organization\",\"site_admin\":false},\"network_count\":0,\"subscribers_count\":1}\n");
        return mock;
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
