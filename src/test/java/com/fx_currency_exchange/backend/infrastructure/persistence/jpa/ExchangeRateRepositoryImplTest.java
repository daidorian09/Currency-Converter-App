package com.fx_currency_exchange.backend.infrastructure.persistence.jpa;

import com.fx_currency_exchange.backend.domain.entity.ExchangeRate;
import com.fx_currency_exchange.backend.infrastructure.persistence.jpa.entity.ExchangeRateEntity;
import com.fx_currency_exchange.backend.infrastructure.persistence.jpa.repository.JpaExchangeRateRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
                .timestamp(LocalDateTime.now())
                .build();

        final ExchangeRateEntity entity2 = ExchangeRateEntity.builder()
                .id(UUID.randomUUID())
                .fromCurrency("USD")
                .toCurrency("EUR")
                .rate(BigDecimal.ONE)
                .timestamp(LocalDateTime.now().plusHours(1))
                .build();

        when(jpaRepository.findTopByFromCurrencyAndToCurrencyOrderByTimestampDesc("USD", "EUR"))
                .thenReturn(Optional.of(entity));

        when(jpaRepository.findTopByFromCurrencyAndToCurrencyOrderByTimestampDesc("USD", "EUR"))
                .thenReturn(Optional.of(entity2));

        final Optional<ExchangeRate> result = repository.findByCurrencyPair("USD", "EUR");

        assertTrue(result.isPresent());
        assertEquals(entity2.getId(), result.get().getId());
        assertEquals("USD", result.get().getFromCurrency());
        assertEquals("EUR", result.get().getToCurrency());
        assertEquals(BigDecimal.ONE, result.get().getRate());
    }

    @Test
    void shouldReturnEmptyIfCurrencyPairNotFound() {
        when(jpaRepository.findTopByFromCurrencyAndToCurrencyOrderByTimestampDesc("USD", "JPY"))
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
                .timestamp(LocalDateTime.now())
                .build();

        repository.save(rate);

        verify(jpaRepository).save(ArgumentMatchers.any(ExchangeRateEntity.class));
    }
}