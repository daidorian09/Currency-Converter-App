package com.fx_currency_exchange.backend.domain.exception;

public class InvalidCurrencyCodeException extends BaseFxCurrencyExchangeException {
    public InvalidCurrencyCodeException(final String currencyCode) {
        super("%s is invalid. Currency code must be a 3-letter uppercase ISO code".formatted(currencyCode));
    }
}