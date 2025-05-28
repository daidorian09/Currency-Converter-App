package com.fx_currency_exchange.backend.application.mapper;

import com.fx_currency_exchange.backend.application.dto.response.ExchangeRateResponse;
import com.fx_currency_exchange.backend.domain.entity.ExchangeRate;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ExchangeRateResponseMapperTest {
    @Test
    void shouldMapToExchangeRateResponse() {
        final ExchangeRate exchangeRate = ExchangeRate.builder()
                .id(UUID.randomUUID())
                .fromCurrency("USD")
                .toCurrency("EUR")
                .rate(BigDecimal.valueOf(0.92))
                .build();

        ExchangeRateResponse response = ExchangeRateResponseMapper.from(exchangeRate);

        assertThat(response).isNotNull();
        assertThat(response.fromCurrency()).isEqualTo("USD");
        assertThat(response.toCurrency()).isEqualTo("EUR");
        assertThat(response.rate()).isEqualTo(BigDecimal.valueOf(0.92));
    }
}