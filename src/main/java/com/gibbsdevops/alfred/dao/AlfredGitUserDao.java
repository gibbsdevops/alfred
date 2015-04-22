package com.gibbsdevops.alfred.dao;

import com.gibbsdevops.alfred.model.alfred.AlfredGitUser;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.repository.CrudRepository;

@CacheConfig(cacheNames = "AlfredGitUser")
public interface AlfredGitUserDao extends CrudRepository<AlfredGitUser, Long> {

    @Override
    @Caching(put = {
            @CachePut(key = "#result.id"),
            @CachePut(key = "#result.name + '|' + #result.email")
    })
    AlfredGitUser save(AlfredGitUser u);

    @Override
    @Cacheable(key = "#p0")
    AlfredGitUser findOne(Long id);

    @Cacheable(key = "#p0 + '|' + #p1")
    AlfredGitUser getByNameAndEmail(String name, String email);

}
