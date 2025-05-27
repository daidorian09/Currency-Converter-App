package com.fx_currency_exchange.backend.infrastructure.persistence.mapper;

import com.fx_currency_exchange.backend.domain.entity.ConversionTransaction;
import com.fx_currency_exchange.backend.infrastructure.persistence.jpa.entity.ConversionTransactionEntity;

public class ConversionTransactionMapper {

    public static ConversionTransactionEntity toEntity(final ConversionTransaction model) {
        return ConversionTransactionEntity.builder()
                .id(model.getId())
                .fromCurrency(model.getFromCurrency())
                .toCurrency(model.getToCurrency())
                .amount(model.getAmount())
                .convertedAmount(model.getConvertedAmount())
                .timestamp(model.getTimestamp())
                .build();
    }

    public static ConversionTransaction toDomain(final ConversionTransactionEntity entity) {
        return ConversionTransaction.builder()
                .id(entity.getId())
                .fromCurrency(entity.getFromCurrency())
                .toCurrency(entity.getToCurrency())
                .amount(entity.getAmount())
                .convertedAmount(entity.getConvertedAmount())
                .timestamp(entity.getTimestamp())
                .build();
    }
}