package com.fx_currency_exchange.backend.api.exception;

import com.fx_currency_exchange.backend.domain.exception.BaseFxCurrencyExchangeException;
import com.fx_currency_exchange.backend.domain.exception.ExchangeRateNotFound;
import com.fx_currency_exchange.backend.domain.exception.InvalidCurrencyCodeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void shouldHandleBaseFxCurrencyExchangeException() {
        final BaseFxCurrencyExchangeException ex = new InvalidCurrencyCodeException("XYZ");

        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleDomainExceptions(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().containsKey("error"));
        assertEquals("InvalidCurrencyCodeException", response.getBody().get("error"));
        assertTrue(response.getBody().get("message").toString().contains("XYZ"));
    }

    @Test
    void shouldHandleExchangeRateNotFoundException() {
        final ExchangeRateNotFound ex = new ExchangeRateNotFound("USD", "TRY");

        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleExchangeRateNotFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("ExchangeRateNotFound", response.getBody().get("error"));
        assertTrue(response.getBody().get("message").toString().contains("USD"));
    }

    @Test
    void shouldHandleGenericException() {
        final Exception ex = new RuntimeException("Unexpected error");

        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleGenericException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("RuntimeException", response.getBody().get("error"));
        assertEquals("Unexpected error", response.getBody().get("message"));
    }
}