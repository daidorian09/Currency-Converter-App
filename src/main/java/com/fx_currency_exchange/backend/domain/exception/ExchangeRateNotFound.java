package com.fx_currency_exchange.backend.domain.exception;

public class ExchangeRateNotFound extends RuntimeException {
    public ExchangeRateNotFound(final String fromCurrency, final String toCurrency) {
        super("Exchange rate not found for %s to %s".formatted(fromCurrency, toCurrency));
    }
}