package com.fx_currency_exchange.backend.application.mapper;

import com.fx_currency_exchange.backend.application.dto.response.CurrencyConversionResponse;
import com.fx_currency_exchange.backend.domain.entity.ConversionTransaction;

public class CurrencyConversionResponseMapper {
    public static CurrencyConversionResponse from(final ConversionTransaction tx) {
        return new CurrencyConversionResponse(
                tx.getId(),
                tx.getConvertedAmount()
        );
    }
}
