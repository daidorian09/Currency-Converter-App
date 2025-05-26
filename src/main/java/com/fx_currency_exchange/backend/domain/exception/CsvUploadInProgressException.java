package com.fx_currency_exchange.backend.domain.exception;

public class CsvUploadInProgressException extends BaseFxCurrencyExchangeException {
    public CsvUploadInProgressException(String filename) {
        super("CSV upload is already in progress for file: %s".formatted(filename));
    }
}