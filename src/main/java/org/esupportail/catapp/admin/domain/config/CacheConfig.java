package org.esupportail.catapp.admin.domain.config;

import net.sf.ehcache.CacheException;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Lazy
@Configuration
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() throws CacheException, IOException {
        EhCacheManagerFactoryBean factory = new EhCacheManagerFactoryBean();
        factory.setConfigLocation(new ClassPathResource("/META-INF/ehcache.xml"));
        factory.afterPropertiesSet();
        return new EhCacheCacheManager(factory.getObject());
    }

    @Bean
    public Cache mailCache() throws CacheException, IOException {
        return cacheManager().getCache("mailCache");
    }
}
