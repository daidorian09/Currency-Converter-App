package com.fx_currency_exchange.backend.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response for CSV upload summary")
public record CsvUploadResponse(
        @Schema(description = "Number of successful records processed", example = "3")
        int successCount,

        @Schema(description = "Number of failed records", example = "1")
        int failureCount
) {
}