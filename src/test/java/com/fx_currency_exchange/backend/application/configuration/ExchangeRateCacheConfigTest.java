package com.fx_currency_exchange.backend.application.configuration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@TestPropertySource(properties = {
        "cache.exchange-rate.ttl=600000",
        "cache.exchange-rate.idle=300000"
})
@EnableConfigurationProperties(ExchangeRateCacheConfig.class)
class ExchangeRateCacheConfigTest {

    @Autowired
    private ExchangeRateCacheConfig config;

    @Test
    void shouldBindTtlAndIdleProperties() {
        assertThat(config.getTtl()).isEqualTo(600_000);
        assertThat(config.getIdle()).isEqualTo(300_000);
    }
}