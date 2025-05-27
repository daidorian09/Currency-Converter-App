package com.fx_currency_exchange.backend.infrastructure.persistence.jpa;

import com.fx_currency_exchange.backend.domain.entity.ExchangeRate;
import com.fx_currency_exchange.backend.infrastructure.persistence.jpa.entity.ExchangeRateEntity;
import com.fx_currency_exchange.backend.infrastructure.persistence.jpa.repository.JpaExchangeRateRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ExchangeRateRepositoryImplTest {
    private final JpaExchangeRateRepository jpaRepository = mock(JpaExchangeRateRepository.class);
    private final ExchangeRateRepositoryImpl repository = new ExchangeRateRepositoryImpl(jpaRepository);

    @Test
    void shouldFindByCurrencyPairAndReturnDomainObject() {
        final ExchangeRateEntity entity = ExchangeRateEntity.builder()
                .id(UUID.randomUUID())
                .fromCurrency("USD")
                .toCurrency("EUR")
                .rate(new BigDecimal("0.92"))
                .build();

        when(jpaRepository.findByFromCurrencyAndToCurrency("USD", "EUR"))
                .thenReturn(Optional.of(entity));

        final Optional<ExchangeRate> result = repository.findByCurrencyPair("USD", "EUR");

        assertTrue(result.isPresent());
        assertEquals("USD", result.get().getFromCurrency());
        assertEquals("EUR", result.get().getToCurrency());
        assertEquals(new BigDecimal("0.92"), result.get().getRate());
    }

    @Test
    void shouldReturnEmptyIfCurrencyPairNotFound() {
        when(jpaRepository.findByFromCurrencyAndToCurrency("USD", "JPY"))
                .thenReturn(Optional.empty());

        final Optional<ExchangeRate> result = repository.findByCurrencyPair("USD", "JPY");

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldSaveExchangeRateEntity() {
        final ExchangeRate rate = ExchangeRate.builder()
                .id(UUID.randomUUID())
                .fromCurrency("GBP")
                .toCurrency("TRY")
                .rate(new BigDecimal("36.5"))
                .build();

        repository.save(rate);

        verify(jpaRepository).save(ArgumentMatchers.any(ExchangeRateEntity.class));
    }
}