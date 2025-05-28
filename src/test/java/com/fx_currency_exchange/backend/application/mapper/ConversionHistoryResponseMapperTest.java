package com.fx_currency_exchange.backend.application.mapper;

import com.fx_currency_exchange.backend.application.dto.response.ConversionHistoryResponse;
import com.fx_currency_exchange.backend.domain.entity.ConversionTransaction;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ConversionHistoryResponseMapperTest {
    @Test
    void shouldMapDomainToResponseCorrectly() {
        final UUID id = UUID.randomUUID();
        final String fromCurrency = "USD";
        final String toCurrency = "EUR";
        final BigDecimal amount = BigDecimal.valueOf(100);
        final BigDecimal convertedAmount = BigDecimal.valueOf(92);
        final LocalDateTime timestamp = LocalDateTime.now();

        final ConversionTransaction tx = ConversionTransaction.builder()
                .id(id)
                .fromCurrency(fromCurrency)
                .toCurrency(toCurrency)
                .amount(amount)
                .convertedAmount(convertedAmount)
                .timestamp(timestamp)
                .build();

        final ConversionHistoryResponse response = ConversionHistoryResponseMapper.from(tx);

        assertThat(response).isNotNull();
        assertThat(response.transactionId()).isEqualTo(id);
        assertThat(response.fromCurrency()).isEqualTo(fromCurrency);
        assertThat(response.toCurrency()).isEqualTo(toCurrency);
        assertThat(response.amount()).isEqualTo(amount);
        assertThat(response.convertedAmount()).isEqualTo(convertedAmount);
        assertThat(response.timestamp()).isEqualTo(timestamp);
    }
}