package com.fx_currency_exchange.backend.infrastructure.util;

import com.fx_currency_exchange.backend.domain.exception.InvalidCurrencyCodeException;
import com.fx_currency_exchange.backend.domain.exception.InvalidCurrencyConversionAmountException;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CurrencyValidatorUtilTest {
    @Test
    void shouldPassForValidCurrencyCode() {
        assertDoesNotThrow(() -> CurrencyValidatorUtil.validateCurrencyCode("USD"));
        assertDoesNotThrow(() -> CurrencyValidatorUtil.validateCurrencyCode("EUR"));
    }

    @Test
    void shouldThrowExceptionForInvalidCurrencyCode() {
        assertThrows(InvalidCurrencyCodeException.class, () -> CurrencyValidatorUtil.validateCurrencyCode(null));
        assertThrows(InvalidCurrencyCodeException.class, () -> CurrencyValidatorUtil.validateCurrencyCode(StringUtils.EMPTY));
        assertThrows(InvalidCurrencyCodeException.class, () -> CurrencyValidatorUtil.validateCurrencyCode("usd"));
        assertThrows(InvalidCurrencyCodeException.class, () -> CurrencyValidatorUtil.validateCurrencyCode("US"));
        assertThrows(InvalidCurrencyCodeException.class, () -> CurrencyValidatorUtil.validateCurrencyCode("USDA"));
    }

    @Test
    void shouldPassForValidAmount() {
        assertDoesNotThrow(() -> CurrencyValidatorUtil.validateAmount(BigDecimal.ONE));
        assertDoesNotThrow(() -> CurrencyValidatorUtil.validateAmount(new BigDecimal("0.01")));
    }

    @Test
    void shouldThrowExceptionForInvalidAmount() {
        assertThrows(InvalidCurrencyConversionAmountException.class, () -> CurrencyValidatorUtil.validateAmount(null));
        assertThrows(InvalidCurrencyConversionAmountException.class, () -> CurrencyValidatorUtil.validateAmount(BigDecimal.ZERO));
        assertThrows(InvalidCurrencyConversionAmountException.class, () -> CurrencyValidatorUtil.validateAmount(new BigDecimal("-1")));
    }
}