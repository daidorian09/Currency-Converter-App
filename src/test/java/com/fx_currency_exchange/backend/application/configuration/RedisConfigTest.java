package com.fx_currency_exchange.backend.application.configuration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@EnableConfigurationProperties(RedisConfig.class)
@TestPropertySource(properties = {
        "redis.host=localhost",
        "redis.port=6379",
        "redis.connection-pool-size=64",
        "redis.connection-minimum-idle-size=16",
        "redis.timeout=3000",
        "redis.retry-count=3",
        "redis.retry-interval=1500"
})
class RedisConfigTest {

    @Autowired
    private RedisConfig redisConfig;

    @Test
    void shouldBindRedisPropertiesCorrectly() {
        assertThat(redisConfig.getHost()).isEqualTo("localhost");
        assertThat(redisConfig.getPort()).isEqualTo(6379);
        assertThat(redisConfig.getConnectionPoolSize()).isEqualTo(64);
        assertThat(redisConfig.getConnectionMinimumIdleSize()).isEqualTo(16);
        assertThat(redisConfig.getTimeout()).isEqualTo(3000);
        assertThat(redisConfig.getRetryCount()).isEqualTo(3);
        assertThat(redisConfig.getRetryInterval()).isEqualTo(1500);
    }
}