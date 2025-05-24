package com.fx_currency_exchange.backend.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Response model for a currency conversion transaction record")
public record ConversionHistoryResponse(

        @Schema(description = "Unique identifier of the transaction", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID transactionId,

        @Schema(description = "Source currency code", example = "USD")
        String fromCurrency,

        @Schema(description = "Target currency code", example = "TRY")
        String toCurrency,

        @Schema(description = "Amount converted from source currency", example = "100.00")
        BigDecimal amount,

        @Schema(description = "Resulting amount in target currency", example = "3200.50")
        BigDecimal convertedAmount,

        @Schema(description = "Timestamp of the conversion", example = "2025-05-24T14:23:00")
        LocalDateTime timestamp
) {
}