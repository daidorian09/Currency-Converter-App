package com.fx_currency_exchange.backend.application.dto.request;

import java.math.BigDecimal;

public record CurrencyConversionRequest(
        String fromCurrency,
        String toCurrency,
        BigDecimal amount
) {
}