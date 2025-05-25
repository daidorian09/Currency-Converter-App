package com.fx_currency_exchange.backend.application.configuration;

import lombok.RequiredArgsConstructor;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RedissonConfig {

    private final RedisConfig redisConfig;

    @Bean
    public RedissonClient redissonClient() {
        final Config config = new Config();
        config.useSingleServer()
                .setAddress(String.format("redis://%s:%d", redisConfig.getHost(), redisConfig.getPort()))
                .setTimeout(redisConfig.getTimeout())
                .setConnectionPoolSize(redisConfig.getConnectionPoolSize())
                .setConnectionMinimumIdleSize(redisConfig.getConnectionMinimumIdleSize())
                .setKeepAlive(Boolean.TRUE)
                .setRetryAttempts(redisConfig.getRetryCount())
                .setRetryInterval(redisConfig.getRetryInterval());

        return Redisson.create(config);
    }
}