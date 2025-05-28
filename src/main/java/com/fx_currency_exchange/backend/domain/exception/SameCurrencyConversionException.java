package com.fx_currency_exchange.backend.domain.exception;

public class SameCurrencyConversionException extends BaseFxCurrencyExchangeException {
    public SameCurrencyConversionException(final String currency) {
        super("Conversion with identical source and target currencies is not allowed: %s".formatted(currency));
    }
}
