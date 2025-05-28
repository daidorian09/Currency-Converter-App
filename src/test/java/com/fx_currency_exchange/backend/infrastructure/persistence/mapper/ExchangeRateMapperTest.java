package com.fx_currency_exchange.backend.infrastructure.persistence.mapper;

import com.fx_currency_exchange.backend.domain.entity.ExchangeRate;
import com.fx_currency_exchange.backend.infrastructure.persistence.jpa.entity.ExchangeRateEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ExchangeRateMapperTest {
    @Test
    void shouldMapModelToEntityCorrectly() {
        final UUID id = UUID.randomUUID();
        final LocalDateTime timestamp = LocalDateTime.now();
        final ExchangeRate model = ExchangeRate.builder()
                .id(id)
                .fromCurrency("USD")
                .toCurrency("EUR")
                .rate(new BigDecimal("0.85"))
                .timestamp(timestamp)
                .build();

        final ExchangeRateEntity entity = ExchangeRateMapper.toEntity(model);

        assertNotNull(entity);
        assertEquals(id, entity.getId());
        assertEquals("USD", entity.getFromCurrency());
        assertEquals("EUR", entity.getToCurrency());
        assertEquals(new BigDecimal("0.85"), entity.getRate());
        assertEquals(timestamp, entity.getTimestamp());
    }

    @Test
    void shouldMapEntityToModelCorrectly() {
        final UUID id = UUID.randomUUID();
        final ExchangeRateEntity entity = ExchangeRateEntity.builder()
                .id(id)
                .fromCurrency("GBP")
                .toCurrency("TRY")
                .rate(new BigDecimal("36.70"))
                .build();

        final ExchangeRate model = ExchangeRateMapper.toDomain(entity);

        assertNotNull(model);
        assertEquals(id, model.getId());
        assertEquals("GBP", model.getFromCurrency());
        assertEquals("TRY", model.getToCurrency());
        assertEquals(new BigDecimal("36.70"), model.getRate());
    }
}