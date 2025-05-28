package com.fx_currency_exchange.backend.application.configuration;

import org.junit.jupiter.api.Test;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CacheConfigurationTest {
    @Autowired
    private CacheManager cacheManager;

    @Test
    void shouldUseRedissonSpringCacheManager() {
        assertThat(cacheManager).isInstanceOf(RedissonSpringCacheManager.class);
    }
}