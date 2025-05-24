package com.fx_currency_exchange.backend.domain.exception;

public class InvalidCSVFormatException extends BaseFxCurrencyExchangeException {
    public InvalidCSVFormatException(final int length, final String line) {
        super("Invalid CSV format. Expected 3 columns but found %d : %s".formatted(length, line));
    }
}