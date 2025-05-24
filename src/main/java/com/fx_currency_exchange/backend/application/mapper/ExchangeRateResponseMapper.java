package com.fx_currency_exchange.backend.application.mapper;

import com.fx_currency_exchange.backend.application.dto.response.ExchangeRateResponse;
import com.fx_currency_exchange.backend.domain.entity.ExchangeRate;

public class ExchangeRateResponseMapper {
    public static ExchangeRateResponse from(final ExchangeRate rate) {
        return new ExchangeRateResponse(
                rate.getFromCurrency(),
                rate.getToCurrency(),
                rate.getRate()
        );
    }
}
