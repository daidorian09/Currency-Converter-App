package com.fx_currency_exchange.backend.application.service.impl;

import com.fx_currency_exchange.backend.application.dto.request.CreateExchangeRateRequest;
import com.fx_currency_exchange.backend.application.dto.request.ExchangeRateRequest;
import com.fx_currency_exchange.backend.domain.entity.ExchangeRate;
import com.fx_currency_exchange.backend.domain.exception.ExchangeRateNotFound;
import com.fx_currency_exchange.backend.domain.service.ExchangeRateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ExchangeRateServiceImplTest {
    private ExchangeRateRepository exchangeRateRepository;
    private ExchangeRateServiceImpl service;

    @BeforeEach
    void setUp() {
        exchangeRateRepository = mock(ExchangeRateRepository.class);
        service = new ExchangeRateServiceImpl(exchangeRateRepository);
    }

    @Test
    void getExchangeRate_shouldReturnRateIfExistsInRepository() {
        final ExchangeRateRequest request = new ExchangeRateRequest("USD", "TRY");
        final ExchangeRate rate = ExchangeRate.builder()
                .id(UUID.randomUUID())
                .fromCurrency("USD")
                .toCurrency("TRY")
                .rate(BigDecimal.valueOf(30))
                .build();

        when(exchangeRateRepository.findByCurrencyPair("USD", "TRY"))
                .thenReturn(Optional.of(rate));

        final ExchangeRate result = service.getExchangeRate(request);

        assertNotNull(result);
        assertEquals("USD", result.getFromCurrency());
        assertEquals("TRY", result.getToCurrency());
        assertEquals(0, BigDecimal.valueOf(30).compareTo(result.getRate()));
    }

    @Test
    void getExchangeRate_shouldThrowExceptionIfRateNotFound() {
        final ExchangeRateRequest request = new ExchangeRateRequest("USD", "TRY");

        when(exchangeRateRepository.findByCurrencyPair("USD", "TRY"))
                .thenReturn(Optional.empty());

        assertThrows(ExchangeRateNotFound.class,
                () -> service.getExchangeRate(request));
    }

    @Test
    void createExchangeRate_shouldPersistAndReturnExchangeRate() {
        final CreateExchangeRateRequest request = new CreateExchangeRateRequest("USD", "TRY", BigDecimal.valueOf(30));

        final ExchangeRate result = service.createExchangeRate(request);

        assertNotNull(result);
        assertEquals("USD", result.getFromCurrency());
        assertEquals("TRY", result.getToCurrency());
        assertEquals(0, BigDecimal.valueOf(30).compareTo(result.getRate()));
        verify(exchangeRateRepository).save(any(ExchangeRate.class));
    }

    @Test
    void updateCacheableExchangeRate_shouldReturnSameObjectAndLog() {
        final ExchangeRate rate = ExchangeRate.builder()
                .fromCurrency("USD")
                .toCurrency("TRY")
                .rate(BigDecimal.valueOf(30))
                .build();

        ExchangeRate result = service.updateCacheableExchangeRate(rate);

        assertNotNull(result);
        assertEquals(rate, result);
    }
}