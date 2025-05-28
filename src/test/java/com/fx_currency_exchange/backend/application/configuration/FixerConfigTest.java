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
        "external.fixer.api-key=test-api-key",
        "external.fixer.base-url=https://test-api.fixer.io"
})
@EnableConfigurationProperties(FixerConfig.class)
class FixerConfigTest {

    @Autowired
    private FixerConfig fixerConfig;

    @Test
    void shouldBindFixerPropertiesCorrectly() {
        assertThat(fixerConfig.getApiKey()).isEqualTo("test-api-key");
        assertThat(fixerConfig.getBaseUrl()).isEqualTo("https://test-api.fixer.io");
    }
}