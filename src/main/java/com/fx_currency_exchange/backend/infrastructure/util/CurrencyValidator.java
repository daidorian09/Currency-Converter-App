package com.fx_currency_exchange.backend.infrastructure.util;

import com.fx_currency_exchange.backend.domain.exception.InvalidCurrencyCodeException;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import static com.fx_currency_exchange.backend.application.constant.ApplicationConstant.CURRENCY_CODE_LENGTH;

@UtilityClass
public class CurrencyValidator {
    public void validateCurrencyCode(final String currencyCode) {
        if (StringUtils.isBlank(currencyCode) || currencyCode.length() != CURRENCY_CODE_LENGTH) {
            throw new InvalidCurrencyCodeException(currencyCode);
        }
    }
}
