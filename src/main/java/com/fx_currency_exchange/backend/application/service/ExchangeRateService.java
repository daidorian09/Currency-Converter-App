package com.fx_currency_exchange.backend.application.service;

import com.fx_currency_exchange.backend.application.dto.request.ExchangeRateRequest;
import com.fx_currency_exchange.backend.domain.entity.ExchangeRate;

public interface ExchangeRateService {
    ExchangeRate getExchangeRate(final ExchangeRateRequest request);
}
