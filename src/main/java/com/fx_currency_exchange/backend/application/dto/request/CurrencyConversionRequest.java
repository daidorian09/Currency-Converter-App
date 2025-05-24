package com.fx_currency_exchange.backend.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Request model for currency conversion")
public record CurrencyConversionRequest(

        @Schema(description = "Source currency code in 3-letter format (e.g., USD)", example = "USD")
        String fromCurrency,

        @Schema(description = "Target currency code in 3-letter format (e.g., EUR)", example = "EUR")
        String toCurrency,

        @Schema(description = "Amount to convert", example = "100.50")
        BigDecimal amount
) {
}