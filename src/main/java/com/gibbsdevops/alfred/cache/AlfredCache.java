package com.gibbsdevops.alfred.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.concurrent.ConcurrentMapCache;

import java.util.concurrent.ConcurrentMap;

public class AlfredCache extends ConcurrentMapCache implements Cache {

    private static final Logger LOG = LoggerFactory.getLogger(AlfredCache.class);

    public AlfredCache(String name) {
        super(name);
    }

    public AlfredCache(String name, boolean allowNullValues) {
        super(name, allowNullValues);
    }

    public AlfredCache(String name, ConcurrentMap<Object, Object> store, boolean allowNullValues) {
        super(name, store, allowNullValues);
    }

    @Override
    public ValueWrapper get(Object key) {
        ValueWrapper valueWrapper = super.get(key);
        if (valueWrapper != null && valueWrapper.get() != null) {
            LOG.debug("{} Cache HIT on {}", getName(), key);
        } else {
            LOG.debug("{} Cache MISS on {}", getName(), key);
        }
        return valueWrapper;
    }

    @Override
    public void put(Object key, Object value) {
        LOG.debug("{} Cache NEW on {}", getName(), key);
        super.put(key, value);
    }


    @Override
    public <T> T get(Object key, Class<T> type) {
        LOG.debug("get Class - {}", key);
        return super.get(key, type);
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        LOG.debug("putIfAbsent");
        return super.putIfAbsent(key, value);
    }

    @Override
    public void evict(Object key) {
        LOG.info("{} Cache evict on {}", getName(), key);
        super.evict(key);
    }

    @Override
    public void clear() {
        LOG.debug("clear");
        super.clear();
    }

    @Override
    protected Object fromStoreValue(Object storeValue) {
        return super.fromStoreValue(storeValue);
    }

    @Override
    protected Object toStoreValue(Object userValue) {
        return super.toStoreValue(userValue);
    }
}
