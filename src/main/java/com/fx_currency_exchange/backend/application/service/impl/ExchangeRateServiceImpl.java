package com.fx_currency_exchange.backend.application.service.impl;

import com.fx_currency_exchange.backend.application.dto.request.ExchangeRateRequest;
import com.fx_currency_exchange.backend.application.service.ExchangeRateService;
import com.fx_currency_exchange.backend.domain.entity.ExchangeRate;
import com.fx_currency_exchange.backend.domain.exception.ExchangeRateNotFound;
import com.fx_currency_exchange.backend.domain.service.ExchangeRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExchangeRateServiceImpl implements ExchangeRateService {

    private final ExchangeRateRepository exchangeRateRepository;

    @Override
    public ExchangeRate getExchangeRate(final ExchangeRateRequest request) {
        return exchangeRateRepository
                .findByCurrencyPair(request.fromCurrency(), request.toCurrency())
                .orElseThrow(() -> new ExchangeRateNotFound(request.fromCurrency(), request.toCurrency()));
    }
}