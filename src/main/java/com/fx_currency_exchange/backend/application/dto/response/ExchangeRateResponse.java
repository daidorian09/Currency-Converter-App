package com.fx_currency_exchange.backend.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Response model for exchange rate lookup")
public record ExchangeRateResponse(

        @Schema(description = "Source currency", example = "USD")
        String fromCurrency,

        @Schema(description = "Target currency", example = "EUR")
        String toCurrency,

        @Schema(description = "Exchange rate value", example = "1.0893")
        BigDecimal rate

) {
}