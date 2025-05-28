package com.fx_currency_exchange.backend.application.configuration;

import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(properties = {
        "redis.host=localhost",
        "redis.port=6379",
        "redis.timeout=5000",
        "redis.connectionPoolSize=32",
        "redis.connectionMinimumIdleSize=8",
        "redis.retryCount=3",
        "redis.retryInterval=1000"
})
class RedissonConfigTest {

    @Autowired
    private RedissonClient redissonClient;

    @Test
    void shouldLoadRedissonClientWithExpectedConfiguration() {
        final Config config = redissonClient.getConfig();

        assertThat(config.useSingleServer().getAddress()).isEqualTo("redis://localhost:6379");
        assertThat(config.useSingleServer().getTimeout()).isEqualTo(5000);
        assertThat(config.useSingleServer().getConnectionPoolSize()).isEqualTo(32);
        assertThat(config.useSingleServer().getConnectionMinimumIdleSize()).isEqualTo(8);
        assertThat(config.useSingleServer().getRetryAttempts()).isEqualTo(3);
        assertThat(config.useSingleServer().getRetryInterval()).isEqualTo(1000);
    }
}