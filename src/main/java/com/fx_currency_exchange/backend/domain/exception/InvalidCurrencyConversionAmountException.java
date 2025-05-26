package com.fx_currency_exchange.backend.domain.exception;

import java.math.BigDecimal;

public class InvalidCurrencyConversionAmountException extends BaseFxCurrencyExchangeException {
    public InvalidCurrencyConversionAmountException(final BigDecimal amount) {
        super("Amount must be a positive number : %s".formatted(amount));
    }
}