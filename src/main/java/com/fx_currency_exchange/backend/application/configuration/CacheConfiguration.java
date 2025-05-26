package com.fx_currency_exchange.backend.application.configuration;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonClient;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

import static com.fx_currency_exchange.backend.application.constant.ApplicationConstant.EXCHANGE_RATE_CACHE_NAME;

@Configuration
@RequiredArgsConstructor
public class CacheConfiguration {

    private final ExchangeRateCacheConfig exchangeRateCacheConfig;

    @Bean
    public CacheManager cacheManager(RedissonClient redissonClient) {
        final Map<String, CacheConfig> config = new HashMap<>();
        config.put(EXCHANGE_RATE_CACHE_NAME,
                new CacheConfig(exchangeRateCacheConfig.getTtl(), exchangeRateCacheConfig.getIdle()));
        return new RedissonSpringCacheManager(redissonClient, config);
    }
}