package com.fx_currency_exchange.backend.domain.entity;

import com.fx_currency_exchange.backend.domain.exception.InvalidCurrencyCodeException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ExchangeRateTest {
    @Test
    void shouldCreateExchangeRateWithValidInputs() {
        final UUID id = UUID.randomUUID();
        final String from = "USD";
        final String to = "EUR";
        final BigDecimal rate = BigDecimal.valueOf(1.08);

        final ExchangeRate exchangeRate = ExchangeRate.builder()
                .id(id)
                .fromCurrency(from)
                .toCurrency(to)
                .rate(rate)
                .build();

        assertThat(exchangeRate.getId()).isEqualTo(id);
        assertThat(exchangeRate.getFromCurrency()).isEqualTo("USD");
        assertThat(exchangeRate.getToCurrency()).isEqualTo("EUR");
        assertThat(exchangeRate.getRate()).isEqualTo(rate);
    }

    @Test
    void shouldAutoGenerateIdAndTimestampIfNotProvided() {
        final ExchangeRate exchangeRate = ExchangeRate.builder()
                .fromCurrency("GBP")
                .toCurrency("TRY")
                .rate(BigDecimal.valueOf(35.4))
                .build();

        assertThat(exchangeRate.getId()).isNotNull();
        assertThat(exchangeRate.getFromCurrency()).isEqualTo("GBP");
        assertThat(exchangeRate.getToCurrency()).isEqualTo("TRY");
        assertThat(exchangeRate.getRate()).isEqualTo(BigDecimal.valueOf(35.4));
        assertThat(exchangeRate.getTimestamp()).isNotNull();

    }

    @Test
    void shouldThrowExceptionForInvalidFromCurrency() {
        assertThrows(InvalidCurrencyCodeException.class, () -> ExchangeRate.builder()
                .fromCurrency("invalid")
                .toCurrency("EUR")
                .rate(BigDecimal.TEN)
                .build());
    }

    @Test
    void shouldThrowExceptionForInvalidToCurrency() {
        assertThrows(InvalidCurrencyCodeException.class, () -> ExchangeRate.builder()
                .fromCurrency("USD")
                .toCurrency("123")
                .rate(BigDecimal.ONE)
                .build());
    }
}