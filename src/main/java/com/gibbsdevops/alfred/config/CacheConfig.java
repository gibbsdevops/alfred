package com.gibbsdevops.alfred.config;

import com.gibbsdevops.alfred.cache.AlfredCache;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        Set<Cache> caches = new HashSet<>();
        caches.add(new AlfredCache("AlfredGitUser"));

        SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
        simpleCacheManager.setCaches(caches);
        simpleCacheManager.afterPropertiesSet();
        return simpleCacheManager;
    }

}
