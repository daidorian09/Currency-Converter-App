package com.fx_currency_exchange.backend.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request model for querying the exchange rate between two currencies")
public record ExchangeRateRequest(

        @Schema(description = "Source currency code in 3-letter format (e.g., USD)", example = "USD")
        String fromCurrency,

        @Schema(description = "Target currency code in 3-letter format (e.g., EUR)", example = "EUR")
        String toCurrency
) {
}