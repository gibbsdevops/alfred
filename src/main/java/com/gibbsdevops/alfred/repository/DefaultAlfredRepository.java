package com.gibbsdevops.alfred.repository;

import com.gibbsdevops.alfred.model.alfred.AlfredGitUser;
import com.gibbsdevops.alfred.model.alfred.AlfredRepoNode;
import com.gibbsdevops.alfred.model.alfred.AlfredUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class DefaultAlfredRepository implements AlfredRepository {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultAlfredRepository.class);

    @Override
    public AlfredUser save(AlfredUser user) {
        LOG.info("Saving user name={}, login={}", user.getName(), user.getLogin());
        return user;
    }

    @Override
    public AlfredRepoNode save(AlfredRepoNode repo) {
        LOG.info("Saving repo {}", repo.getName());
        return repo;
    }

    @Override
    public AlfredGitUser save(AlfredGitUser user) {
        LOG.info("Saving git user {}", user.getName());
        return user;
    }

    @Override
    public AlfredUser getOrgByName(String name) {
        LOG.info("Getting org {}", name);
        return null;
    }

    @Override
    public AlfredUser getUserByName(String name) {
        LOG.info("Getting user {}", name);
        return null;
    }

}
