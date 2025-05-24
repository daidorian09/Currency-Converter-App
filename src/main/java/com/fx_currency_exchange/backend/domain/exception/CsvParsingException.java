package com.fx_currency_exchange.backend.domain.exception;

public class CsvParsingException extends BaseFxCurrencyExchangeException {
    public CsvParsingException(final String fileName) {
        super("Failed to parse CSV file: %s".formatted(fileName));
    }
}