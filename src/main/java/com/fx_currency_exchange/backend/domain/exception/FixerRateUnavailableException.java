package com.fx_currency_exchange.backend.domain.exception;

public class FixerRateUnavailableException extends BaseFxCurrencyExchangeException {
    public FixerRateUnavailableException(String from, String to) {
        super("Missing currency rates in Fixer response for %s or %s".formatted(from, to));
    }

    public FixerRateUnavailableException() {
        super("Failed to fetch exchange rates from Fixer.");
    }
}
