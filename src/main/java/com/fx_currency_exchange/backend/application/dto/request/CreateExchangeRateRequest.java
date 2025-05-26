package com.fx_currency_exchange.backend.application.dto.request;

import java.math.BigDecimal;

public record CreateExchangeRateRequest(String fromCurrency, String toCurrency, BigDecimal rate) {
}
