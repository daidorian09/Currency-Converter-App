package com.fx_currency_exchange.backend.application.configuration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@TestPropertySource(properties = {
        "exchange.job.cron=0 0 * * * *",
        "exchange.job.currency-pairs[0]=USD,TRY",
        "exchange.job.currency-pairs[1]=EUR,USD"
})
@EnableConfigurationProperties(ExchangeRateJobConfig.class)
class ExchangeRateJobConfigTest {

    @Autowired
    private ExchangeRateJobConfig config;

    @Test
    void shouldBindExchangeJobPropertiesCorrectly() {
        assertThat(config.getCron()).isEqualTo("0 0 * * * *");

        List<String[]> pairs = config.getCurrencyPairs();
        assertThat(pairs).hasSize(2);
        assertThat(pairs.get(0)).containsExactly("USD", "TRY");
        assertThat(pairs.get(1)).containsExactly("EUR", "USD");
    }
}