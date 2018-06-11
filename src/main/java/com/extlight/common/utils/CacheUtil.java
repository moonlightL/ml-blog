package com.extlight.common.utils;


import com.extlight.web.context.SpringContext;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

public class CacheUtil {

    public static void deleteByName(String cacheName) {
        CacheManager cacheManager = SpringContext.getBeanByType(CacheManager.class);
        Cache cache = cacheManager.getCache(cacheName);
        cache.removeAll();
        cache.flush();
    }


    public static void deleteAll() {
        CacheManager cacheManager = SpringContext.getBeanByType(CacheManager.class);
        String[] cacheNames = cacheManager.getCacheNames();
        for (String cacheName : cacheNames) {
            Cache cache = cacheManager.getCache(cacheName);
            cache.removeAll();
            cache.flush();
        }
    }
}
