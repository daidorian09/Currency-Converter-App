package com.fx_currency_exchange.backend.infrastructure.persistence.mapper;

import com.fx_currency_exchange.backend.domain.entity.ExchangeRate;
import com.fx_currency_exchange.backend.infrastructure.persistence.jpa.entity.ExchangeRateEntity;

public class ExchangeRateMapper {

    public static ExchangeRateEntity toEntity(final ExchangeRate model) {
        return ExchangeRateEntity.builder()
                .id(model.getId())
                .fromCurrency(model.getFromCurrency())
                .toCurrency(model.getToCurrency())
                .rate(model.getRate())
                .build();
    }

    public static ExchangeRate toDomain(final ExchangeRateEntity entity) {
        return ExchangeRate.builder()
                .id(entity.getId())
                .fromCurrency(entity.getFromCurrency())
                .toCurrency(entity.getToCurrency())
                .rate(entity.getRate())
                .build();
    }
}
