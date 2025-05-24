package com.fx_currency_exchange.backend.domain.entity;

import com.fx_currency_exchange.backend.infrastructure.util.CurrencyValidatorUtil;
import lombok.Builder;
import lombok.Getter;

import java.util.Optional;
import java.util.UUID;

@Getter
public class Currency {
    private final UUID id;
    private final String code;

    @Builder
    public Currency(final UUID id, final String code) {
        CurrencyValidatorUtil.validateCurrencyCode(code);
        this.id = Optional.ofNullable(id).orElse(UUID.randomUUID());
        this.code = code.toUpperCase();
    }
}
