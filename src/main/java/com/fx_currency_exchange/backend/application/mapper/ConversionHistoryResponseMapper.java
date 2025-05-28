package com.fx_currency_exchange.backend.application.mapper;

import com.fx_currency_exchange.backend.application.dto.response.ConversionHistoryResponse;
import com.fx_currency_exchange.backend.domain.entity.ConversionTransaction;

public class ConversionHistoryResponseMapper {

    public static ConversionHistoryResponse from(final ConversionTransaction tx) {
        return new ConversionHistoryResponse(
                tx.getId(),
                tx.getFromCurrency(),
                tx.getToCurrency(),
                tx.getAmount(),
                tx.getConvertedAmount(),
                tx.getTimestamp()
        );
    }
}