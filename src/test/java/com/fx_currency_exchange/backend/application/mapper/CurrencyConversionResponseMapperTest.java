package com.fx_currency_exchange.backend.application.mapper;

import com.fx_currency_exchange.backend.application.dto.response.CurrencyConversionResponse;
import com.fx_currency_exchange.backend.domain.entity.ConversionTransaction;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CurrencyConversionResponseMapperTest {
    @Test
    void shouldMapToCurrencyConversionResponse() {
        final UUID id = UUID.randomUUID();
        final BigDecimal convertedAmount = BigDecimal.valueOf(142.75);

        ConversionTransaction transaction = ConversionTransaction.builder()
                .id(id)
                .fromCurrency("USD")
                .toCurrency("TRY")
                .amount(BigDecimal.valueOf(100))
                .convertedAmount(convertedAmount)
                .timestamp(LocalDateTime.now())
                .build();

        CurrencyConversionResponse response = CurrencyConversionResponseMapper.from(transaction);

        assertThat(response).isNotNull();
        assertThat(response.transactionId()).isEqualTo(id);
        assertThat(response.convertedAmount()).isEqualTo(convertedAmount);
    }
}