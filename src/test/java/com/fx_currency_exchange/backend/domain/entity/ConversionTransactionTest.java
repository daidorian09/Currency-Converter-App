package com.fx_currency_exchange.backend.domain.entity;

import com.fx_currency_exchange.backend.domain.exception.InvalidCurrencyCodeException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ConversionTransactionTest {

    @Test
    void shouldCreateConversionTransactionWithValidInputs() {
        final UUID id = UUID.randomUUID();
        final String from = "USD";
        final String to = "TRY";
        final BigDecimal amount = BigDecimal.valueOf(100);
        final BigDecimal converted = BigDecimal.valueOf(3200);
        final LocalDateTime timestamp = LocalDateTime.now();

        final ConversionTransaction transaction = ConversionTransaction.builder()
                .id(id)
                .fromCurrency(from)
                .toCurrency(to)
                .amount(amount)
                .convertedAmount(converted)
                .timestamp(timestamp)
                .build();

        assertThat(transaction.getId()).isEqualTo(id);
        assertThat(transaction.getFromCurrency()).isEqualTo("USD");
        assertThat(transaction.getToCurrency()).isEqualTo("TRY");
        assertThat(transaction.getAmount()).isEqualTo(amount);
        assertThat(transaction.getConvertedAmount()).isEqualTo(converted);
        assertThat(transaction.getTimestamp()).isEqualTo(timestamp);
    }

    @Test
    void shouldGenerateIdAndTimestampIfNotProvided() {
        final ConversionTransaction transaction = ConversionTransaction.builder()
                .fromCurrency("EUR")
                .toCurrency("GBP")
                .amount(BigDecimal.TEN)
                .convertedAmount(BigDecimal.ONE)
                .build();

        assertThat(transaction.getId()).isNotNull();
        assertThat(transaction.getTimestamp()).isNotNull();
        assertThat(transaction.getFromCurrency()).isEqualTo("EUR");
        assertThat(transaction.getToCurrency()).isEqualTo("GBP");
    }

    @Test
    void shouldThrowExceptionForInvalidFromCurrency() {
        assertThrows(InvalidCurrencyCodeException.class, () -> ConversionTransaction.builder()
                .fromCurrency("INVALID")
                .toCurrency("USD")
                .amount(BigDecimal.TEN)
                .convertedAmount(BigDecimal.ONE)
                .build());
    }

    @Test
    void shouldThrowExceptionForInvalidToCurrency() {
        assertThrows(InvalidCurrencyCodeException.class, () -> ConversionTransaction.builder()
                .fromCurrency("USD")
                .toCurrency("123")
                .amount(BigDecimal.TEN)
                .convertedAmount(BigDecimal.ONE)
                .build());
    }
}