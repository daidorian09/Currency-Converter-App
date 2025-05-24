package com.fx_currency_exchange.backend.domain.entity;

import com.fx_currency_exchange.backend.infrastructure.util.CurrencyValidatorUtil;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Getter
public class ConversionTransaction {

    private final UUID id;
    private final String fromCurrency;
    private final String toCurrency;
    private final BigDecimal amount;
    private final BigDecimal convertedAmount;
    private final LocalDateTime timestamp;

    @Builder
    public ConversionTransaction(final UUID id,
                                 final String fromCurrency,
                                 final String toCurrency,
                                 final BigDecimal amount,
                                 final BigDecimal convertedAmount,
                                 final LocalDateTime timestamp) {
        CurrencyValidatorUtil.validateCurrencyCode(fromCurrency);
        CurrencyValidatorUtil.validateCurrencyCode(toCurrency);
        this.id = Optional.ofNullable(id).orElse(UUID.randomUUID());
        this.fromCurrency = fromCurrency.toUpperCase();
        this.toCurrency = toCurrency.toUpperCase();
        this.amount = amount;
        this.convertedAmount = convertedAmount;
        this.timestamp = Optional.ofNullable(timestamp).orElse(LocalDateTime.now());
    }
}