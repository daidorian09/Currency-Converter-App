package com.fx_currency_exchange.backend.application.dto.request;

public record ExchangeRateRequest(
        String fromCurrency,
        String toCurrency
) {
}
