package com.fx_currency_exchange.backend.domain.exception;

public class UploadedFileEmptyException extends BaseFxCurrencyExchangeException {
    public UploadedFileEmptyException(final String fileName) {
        super("%s is empty. Uploaded file cannot be empty.".formatted(fileName));
    }
}