package com.fx_currency_exchange.backend.application.service;

import com.fx_currency_exchange.backend.application.dto.request.ExchangeRateRequest;
import com.fx_currency_exchange.backend.domain.entity.ExchangeRate;

import java.util.Optional;

public interface ExchangeRateService {
    Optional<ExchangeRate> getExchangeRate(final ExchangeRateRequest request);
}
