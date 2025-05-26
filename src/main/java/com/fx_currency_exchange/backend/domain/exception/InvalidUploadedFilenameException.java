package com.fx_currency_exchange.backend.domain.exception;

public class InvalidUploadedFilenameException extends BaseFxCurrencyExchangeException {
    public InvalidUploadedFilenameException() {
        super("Uploaded file must have a valid, non-blank filename.");
    }
}