package com.gibbsdevops.alfred.repository;

import com.gibbsdevops.alfred.dao.*;
import com.gibbsdevops.alfred.model.alfred.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.persistence.OptimisticLockException;
import javax.sql.DataSource;
import java.lang.reflect.InvocationTargetException;

@Repository
public class DefaultAlfredRepository implements AlfredRepository {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultAlfredRepository.class);

    private JdbcTemplate jdbcTemplate;

    private SimpleJdbcInsert insertUser;

    @Autowired
    private AlfredUserDao alfredUserDao;

    @Autowired
    private AlfredGitUserDao alfredGitUserDao;

    @Autowired
    private AlfredRepoDao alfredRepoDao;

    @Autowired
    private AlfredCommitDao alfredCommitDao;

    @Autowired
    private AlfredJobDao alfredJobDao;

    private NullAwareBeanUtilsBean nullAwareBeanUtils;

    public DefaultAlfredRepository() {
        nullAwareBeanUtils = new NullAwareBeanUtilsBean();
    }

    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        insertUser = new SimpleJdbcInsert(dataSource)
                .withTableName("alfred_user");
    }

    public AlfredUser getByLogin(String login) {
        return alfredUserDao.getByLogin(login);
    }

    @Override
    public AlfredUser save(AlfredUser user) {
        LOG.info("Saving user login={}, name={}", user.getLogin(), user.getName());

        if (user.getId() == null) {
            AlfredUser existing = alfredUserDao.getByLogin(user.getLogin());
            if (existing != null) {
                copyProperties(user, existing);
                return alfredUserDao.save(existing);
            }
        }
        return alfredUserDao.save(user);
    }

    @Override
    public AlfredRepo save(AlfredRepo repo) {
        LOG.info("Saving repo {}", repo.getName());
        if (repo.getId() == null) {
            AlfredRepo existing = alfredRepoDao.findByOwnerAndName(repo.getOwner(), repo.getName());
            if (existing != null) {
                copyProperties(repo, existing);
                return alfredRepoDao.save(existing);
            }
        }
        return alfredRepoDao.save(repo);
    }

    @Override
    public AlfredGitUser save(AlfredGitUser user) {
        AlfredGitUser existing = alfredGitUserDao.getByNameAndEmail(user.getName(), user.getEmail());
        if (existing != null) return existing;

        LOG.info("Saving new git user {} <{}>", user.getName(), user.getEmail());

        user = alfredGitUserDao.save(user);
        return user;
    }

    @Override
    public AlfredCommit save(AlfredCommit commit) {
        if (commit == null) throw new NullPointerException();
        LOG.info("Saving commit {}", commit.getHash());

        if (commit.getId() == null) {
            AlfredCommit existing = alfredCommitDao.findByRepoAndHash(commit.getRepo(), commit.getHash());
            if (existing != null) {
                copyProperties(commit, existing);
                return alfredCommitDao.save(existing);
            }
        }
        return alfredCommitDao.save(commit);
    }

    @Override
    public AlfredJob save(AlfredJob job) {
        if (job == null) throw new NullPointerException();
        if (job.getCommit() == null) throw new IllegalArgumentException("Commit can not be null");
        LOG.info("Saving job {}", job.getId());
        try {
            job = alfredJobDao.save(job);
        } catch (OptimisticLockException e) {
            LOG.warn("Failed to save job", e);
        }

        return job;
    }

    @Override
    public AlfredUser getUserByName(String name) {
        LOG.info("Getting user {}", name);
        return null;
    }

    @Override
    public AlfredCommitNode getCommitNode(Long id) {
        LOG.info("Get commit node {}", id);

        AlfredCommit commit = alfredCommitDao.findOne(id);

        if (commit.getCommitter() == null) throw new NullPointerException();
        AlfredGitUser committer = alfredGitUserDao.findOne(commit.getCommitter());
        AlfredGitUser author = alfredGitUserDao.findOne(commit.getAuthor());
        AlfredGitUser pusher = alfredGitUserDao.findOne(commit.getPusher());
        AlfredUser sender = alfredUserDao.findOne(commit.getSender());

        AlfredRepo repo = alfredRepoDao.findOne(commit.getRepo());
        AlfredUser owner = alfredUserDao.findOne(repo.getOwner());

        AlfredRepoNode repoNode = AlfredRepoProperties.fromRepo(repo);
        repoNode.setOwner(owner);

        AlfredCommitNode node = AlfredCommitProperties.fromCommit(commit);
        node.setCommitter(committer);
        node.setAuthor(author);
        node.setPusher(pusher);
        node.setSender(sender);
        node.setRepo(repoNode);

        return node;
    }

    void copyProperties(Object source, Object destination) {
        try {
            nullAwareBeanUtils.copyProperties(destination, source);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to copy bean properties", e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Failed to copy bean properties", e);
        }
    }

}
