package com.fx_currency_exchange.backend.domain.exception;

public class InvalidCurrencyCodeException extends BaseFxCurrencyExchangeException {
    public InvalidCurrencyCodeException(final String currencyCode) {
        super("%s is invalid. Currency code must be 3 characters".formatted(currencyCode));
    }
}
