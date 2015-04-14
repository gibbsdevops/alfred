package com.gibbsdevops.alfred.repository;

import com.gibbsdevops.alfred.model.alfred.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.lang.reflect.InvocationTargetException;

@Repository
public class DefaultAlfredRepository implements AlfredRepository {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultAlfredRepository.class);

    private JdbcTemplate jdbcTemplate;

    private SimpleJdbcInsert insertUser;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        insertUser = new SimpleJdbcInsert(dataSource)
                .withTableName("alfred_user");

    }

    public AlfredUser getById(long id) {
        return jdbcTemplate.queryForObject("select * from alfred_user where id = ?",
                new Object[]{id}, AlfredUser.class);
    }

    @Override
    public AlfredUser save(AlfredUser user) {
        LOG.info("Saving user login={}, name={}", user.getLogin(), user.getName());

        AlfredUser existing = getById(user.getId());

        if (existing != null) {
            if (existing.equals(user)) {
                return existing;
            } else {

                int hashCode = existing.hashCode();

                try {
                    NullAwareBeanUtilsBean.getInstance().copyProperties(existing, user);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Unable to copy properties", e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException("Unable to copy properties", e);
                }

                if (hashCode != existing.hashCode()) {
                    LOG.info("Updating user {}", user.getLogin());
                    // jdbcTemplate.exe
                }

                return existing;
            }

        } else {
            SqlParameterSource params = new BeanPropertySqlParameterSource(user);
            insertUser.execute(params);
            return user;
        }
    }

    @Override
    public AlfredRepoNode save(AlfredRepoNode node) {
        LOG.info("Saving repo {}", node.getName());
        AlfredRepo normal = node.normalize();
        return node;
    }

    @Override
    public AlfredGitUser save(AlfredGitUser user) {
        LOG.info("Saving git user {}", user.getName());
        return user;
    }

    @Override
    public AlfredCommitNode save(AlfredCommitNode commit) {
        LOG.info("Saving commit {}", commit.getId());
        return commit;
    }

    @Override
    public AlfredUser getUserByName(String name) {
        LOG.info("Getting user {}", name);
        return null;
    }

}
