package com.fx_currency_exchange.backend.infrastructure.external.dto.response;

import java.math.BigDecimal;
import java.util.Map;

public record FixerResponse(
        boolean success,
        long timestamp,
        String base,
        String date,
        Map<String, BigDecimal> rates
) {
}