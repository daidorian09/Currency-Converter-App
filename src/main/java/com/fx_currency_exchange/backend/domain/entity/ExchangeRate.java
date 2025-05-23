package com.fx_currency_exchange.backend.domain.entity;

import com.fx_currency_exchange.backend.infrastructure.util.CurrencyValidator;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Getter
public class ExchangeRate {
    private final UUID id;
    private final String fromCurrency;
    private final String toCurrency;
    private final BigDecimal rate;

    @Builder
    public ExchangeRate(final UUID id, final String fromCurrency, final String toCurrency, final BigDecimal rate) {
        CurrencyValidator.validateCurrencyCode(fromCurrency);
        CurrencyValidator.validateCurrencyCode(toCurrency);
        this.id = Optional.ofNullable(id).orElse(UUID.randomUUID());
        this.fromCurrency = fromCurrency.toUpperCase();
        this.toCurrency = toCurrency.toUpperCase();
        this.rate = rate;
    }
}
