package com.fx_currency_exchange.backend.domain.exception;

public class UnsupportedFileTypeException extends BaseFxCurrencyExchangeException {
    public UnsupportedFileTypeException(final String fileType) {
        super("Unsupported file type: %s".formatted(fileType));
    }
}