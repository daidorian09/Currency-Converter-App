package com.fx_currency_exchange.backend.infrastructure.persistence.mapper;

import com.fx_currency_exchange.backend.domain.entity.ConversionTransaction;
import com.fx_currency_exchange.backend.infrastructure.persistence.jpa.entity.ConversionTransactionEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ConversionTransactionMapperTest {

    @Test
    void shouldMapModelToEntityCorrectly() {
        final UUID id = UUID.randomUUID();
        final LocalDateTime timestamp = LocalDateTime.now();
        final ConversionTransaction model = ConversionTransaction.builder()
                .id(id)
                .fromCurrency("USD")
                .toCurrency("TRY")
                .amount(new BigDecimal("100"))
                .convertedAmount(new BigDecimal("3050"))
                .timestamp(timestamp)
                .build();

        ConversionTransactionEntity entity = ConversionTransactionMapper.toEntity(model);

        assertNotNull(entity);
        assertEquals(id, entity.getId());
        assertEquals("USD", entity.getFromCurrency());
        assertEquals("TRY", entity.getToCurrency());
        assertEquals(new BigDecimal("100"), entity.getAmount());
        assertEquals(new BigDecimal("3050"), entity.getConvertedAmount());
        assertEquals(timestamp, entity.getTimestamp());
    }

    @Test
    void shouldMapEntityToModelCorrectly() {
        final UUID id = UUID.randomUUID();
        final LocalDateTime timestamp = LocalDateTime.now();
        final ConversionTransactionEntity entity = ConversionTransactionEntity.builder()
                .id(id)
                .fromCurrency("EUR")
                .toCurrency("GBP")
                .amount(new BigDecimal("50"))
                .convertedAmount(new BigDecimal("42.5"))
                .timestamp(timestamp)
                .build();

        final ConversionTransaction model = ConversionTransactionMapper.toDomain(entity);

        assertNotNull(model);
        assertEquals(id, model.getId());
        assertEquals("EUR", model.getFromCurrency());
        assertEquals("GBP", model.getToCurrency());
        assertEquals(new BigDecimal("50"), model.getAmount());
        assertEquals(new BigDecimal("42.5"), model.getConvertedAmount());
        assertEquals(timestamp, model.getTimestamp());
    }
}