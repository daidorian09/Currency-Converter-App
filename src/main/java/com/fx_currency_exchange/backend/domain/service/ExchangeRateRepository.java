package com.fx_currency_exchange.backend.domain.service;

import com.fx_currency_exchange.backend.domain.entity.ExchangeRate;

import java.util.Optional;

public interface ExchangeRateRepository {
    Optional<ExchangeRate> findByCurrencyPair(final String from, final String to);

    void save(final ExchangeRate exchangeRate);
}
