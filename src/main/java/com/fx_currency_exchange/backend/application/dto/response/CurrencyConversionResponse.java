package com.fx_currency_exchange.backend.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "Response model for a successful currency conversion")
public record CurrencyConversionResponse(

        @Schema(description = "Unique transaction identifier", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID transactionId,

        @Schema(description = "Converted amount in the target currency", example = "124.75")
        BigDecimal convertedAmount

) {
}