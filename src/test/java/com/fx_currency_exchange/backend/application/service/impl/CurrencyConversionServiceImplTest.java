package com.fx_currency_exchange.backend.application.service.impl;

import com.fx_currency_exchange.backend.application.dto.request.CurrencyConversionRequest;
import com.fx_currency_exchange.backend.application.service.CurrencyConversionService;
import com.fx_currency_exchange.backend.domain.entity.ConversionTransaction;
import com.fx_currency_exchange.backend.domain.entity.ExchangeRate;
import com.fx_currency_exchange.backend.domain.exception.ExchangeRateNotFound;
import com.fx_currency_exchange.backend.domain.exception.InvalidCurrencyCodeException;
import com.fx_currency_exchange.backend.domain.exception.InvalidCurrencyConversionAmountException;
import com.fx_currency_exchange.backend.domain.exception.SameCurrencyConversionException;
import com.fx_currency_exchange.backend.domain.service.ConversionTransactionRepository;
import com.fx_currency_exchange.backend.domain.service.ExchangeRateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class CurrencyConversionServiceImplTest {
    private ExchangeRateRepository exchangeRateRepository;
    private ConversionTransactionRepository transactionRepository;
    private CurrencyConversionService service;

    @BeforeEach
    void setUp() {
        exchangeRateRepository = mock(ExchangeRateRepository.class);
        transactionRepository = mock(ConversionTransactionRepository.class);
        service = new CurrencyConversionServiceImpl(exchangeRateRepository, transactionRepository);
    }

    @Test
    void shouldConvertCurrencySuccessfully() {
        final CurrencyConversionRequest request = new CurrencyConversionRequest("USD", "TRY", BigDecimal.valueOf(100));
        ExchangeRate rate = ExchangeRate.builder()
                .id(UUID.randomUUID())
                .fromCurrency("USD")
                .toCurrency("TRY")
                .rate(BigDecimal.valueOf(27))
                .build();

        when(exchangeRateRepository.findByCurrencyPair("USD", "TRY")).thenReturn(Optional.of(rate));

        ConversionTransaction result = service.convert(request);

        assertThat(result).isNotNull();
        assertThat(result.getConvertedAmount()).isEqualByComparingTo("2700");
        verify(exchangeRateRepository).findByCurrencyPair("USD", "TRY");
        verify(transactionRepository).save(any(ConversionTransaction.class));
    }

    @Test
    void shouldThrowWhenFromCurrencyIsInvalid() {
        final CurrencyConversionRequest request = new CurrencyConversionRequest("usd", "EUR", BigDecimal.valueOf(100));

        assertThrows(InvalidCurrencyCodeException.class, () -> service.convert(request));
        verifyNoInteractions(transactionRepository);
    }

    @Test
    void shouldThrowWhenToCurrencyIsInvalid() {
        final CurrencyConversionRequest request = new CurrencyConversionRequest("USD", "eur", BigDecimal.valueOf(100));

        assertThrows(InvalidCurrencyCodeException.class, () -> service.convert(request));
        verifyNoInteractions(transactionRepository);
    }

    @Test
    void shouldThrowWhenCurrenciesAreIdentical() {
        final CurrencyConversionRequest request = new CurrencyConversionRequest("USD", "USD", BigDecimal.valueOf(100));

        assertThrows(SameCurrencyConversionException.class, () -> service.convert(request));
        verifyNoInteractions(transactionRepository);
    }

    @Test
    void shouldThrowWhenAmountIsNull() {
        final CurrencyConversionRequest request = new CurrencyConversionRequest("USD", "EUR", null);

        assertThrows(InvalidCurrencyConversionAmountException.class, () -> service.convert(request));
        verifyNoInteractions(transactionRepository);
    }

    @Test
    void shouldThrowWhenAmountIsLessThanZero() {
        final CurrencyConversionRequest request = new CurrencyConversionRequest("USD", "EUR", new BigDecimal(-1));

        assertThrows(InvalidCurrencyConversionAmountException.class, () -> service.convert(request));
        verifyNoInteractions(transactionRepository);
    }


    @Test
    void shouldThrowWhenExchangeRateNotFound() {
        final CurrencyConversionRequest request = new CurrencyConversionRequest("USD", "XYZ", BigDecimal.valueOf(100));

        when(exchangeRateRepository.findByCurrencyPair("USD", "XYZ")).thenReturn(Optional.empty());

        assertThrows(ExchangeRateNotFound.class, () -> service.convert(request));
        verify(transactionRepository, never()).save(any());
    }
}