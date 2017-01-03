/**
 * @Title: CoreCacheConfiguration.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年6月7日  占志灵
 */

package com.talentwalker.game.md.core.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.springframework.cache.CacheManager;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerUtils;
import org.springframework.cache.interceptor.AbstractCacheResolver;
import org.springframework.cache.interceptor.BasicOperation;
import org.springframework.cache.interceptor.CacheEvictOperation;
import org.springframework.cache.interceptor.CacheOperationInvocationContext;
import org.springframework.cache.interceptor.CachePutOperation;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.CacheableOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.sf.ehcache.Cache;
import net.sf.ehcache.config.CacheConfiguration;

/**
 * @ClassName: CoreCacheConfiguration
 * @Description: Description of this class
 * @author <a href="mailto:XXX@talentwalker.com">占志灵</a> 于 2016年6月7日 上午11:07:29
 */
@Configuration
public class CoreCacheConfiguration {

    // @Bean(name = "sessionCacheResolver")
    // public CacheResolver sessionCacheResolver(CacheManager cacheManager) {
    // System.out.println("sessionCacheResolver");
    // return new SessionCacheResolver(cacheManager);
    // }
    //
    // @Bean
    // public ConcurrentMapCacheManager cacheManager() {
    // System.out.println("cacheManager");
    // ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager();
    // return cacheManager;
    // }

    @Bean(name = "sessionCacheResolver")
    public CacheResolver sessionEhcacheCacheResolver(EhCacheCacheManager cacheManager) {
        // System.out.println("sessionEhcacheCacheResolver");
        return new SessionEhcacheCacheResolver(cacheManager);
    }

    @Bean
    public EhCacheCacheManager cacheManager(net.sf.ehcache.CacheManager ehCacheCacheManager) {
        // System.out.println("cacheManager");
        return new EhCacheCacheManager(ehCacheCacheManager);
    }

    @Bean
    public net.sf.ehcache.CacheManager ehCacheCacheManager() {
        // System.out.println("ehCacheCacheManager");
        net.sf.ehcache.CacheManager cacheManager = EhCacheManagerUtils.buildCacheManager();
        Cache cache = new Cache("dataconfig", 0, false, false, 0, 0);
        cacheManager.addCache(cache);
        return cacheManager;
    }

    public class SessionCacheResolver extends AbstractCacheResolver {

        /**
         * 创建一个新的实例 CoreCacheConfiguration.SessionCacheResolver.
         */
        public SessionCacheResolver(CacheManager cacheManager) {
            super(cacheManager);
        }

        /**.
         * <p>Title: getCacheNames</p>
         * <p>Description: </p>
         * @param context
         * @return
         * @see org.springframework.cache.interceptor.AbstractCacheResolver#getCacheNames(org.springframework.cache.interceptor.CacheOperationInvocationContext)
         */
        @Override
        protected Collection<String> getCacheNames(CacheOperationInvocationContext<?> context) {
            Collection<String> result = new ArrayList<String>();
            String cacheName = null;

            BasicOperation operation = context.getOperation();
            if (operation instanceof CachePutOperation) {
                cacheName = ((String) context.getArgs()[1]).split("-")[0];
            } else if (operation instanceof CacheEvictOperation) {
                cacheName = ((String) context.getArgs()[0]).split("-")[0];
            } else if (operation instanceof CacheableOperation) {
                cacheName = ((String) context.getArgs()[0]).split("-")[0];
            }

            result.add(cacheName);
            return result;
        }

    }

    public class SessionEhcacheCacheResolver implements CacheResolver {

        private CacheConfiguration cacheConfiguration;
        private EhCacheCacheManager cacheManager;

        /**
         * 创建一个新的实例 GameUserServiceRemote.SessionCacheResolver.
         */
        public SessionEhcacheCacheResolver(EhCacheCacheManager cacheManager) {
            this.cacheManager = cacheManager;
            cacheConfiguration = new CacheConfiguration();
            cacheConfiguration.maxEntriesLocalHeap(0).eternal(false).timeToLiveSeconds(0).timeToIdleSeconds(600);
        }

        protected Collection<String> getCacheNames(CacheOperationInvocationContext<?> context) {
            Collection<String> result = new ArrayList<String>();
            String cacheName = null;

            BasicOperation operation = context.getOperation();
            if (operation instanceof CachePutOperation) {
                cacheName = ((String) context.getArgs()[1]).split("-")[0];
            } else if (operation instanceof CacheEvictOperation) {
                Object arg0 = context.getArgs()[0];
                if (arg0 == null) {
                    return null;
                } else {
                    cacheName = ((String) arg0).split("-")[0];
                }
            } else if (operation instanceof CacheableOperation) {
                cacheName = ((String) context.getArgs()[0]).split("-")[0];
            }

            result.add(cacheName);
            return result;
        }

        /**.
         * <p>Title: resolveCaches</p>
         * <p>Description: </p>
         * @param context
         * @return
         * @see org.springframework.cache.interceptor.CacheResolver#resolveCaches(org.springframework.cache.interceptor.CacheOperationInvocationContext)
         */
        @Override
        public Collection<? extends org.springframework.cache.Cache> resolveCaches(
                CacheOperationInvocationContext<?> context) {
            Collection<String> cacheNames = getCacheNames(context);
            if (cacheNames == null) {
                return Collections.emptyList();
            } else {
                Collection<org.springframework.cache.Cache> result = new ArrayList<org.springframework.cache.Cache>();
                for (String cacheName : cacheNames) {
                    org.springframework.cache.Cache cache = cacheManager.getCache(cacheName);
                    if (cache == null) {
                        cacheConfiguration.setName(cacheName);
                        Cache ehCache = new Cache(cacheConfiguration);
                        cacheManager.getCacheManager().addCache(ehCache);
                        cache = cacheManager.getCache(cacheName);
                    }

                    result.add(cache);
                }
                return result;
            }
        }

    }
}
