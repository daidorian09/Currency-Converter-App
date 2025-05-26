package com.fx_currency_exchange.backend.infrastructure.util;

import com.fx_currency_exchange.backend.domain.exception.InvalidCurrencyCodeException;
import com.fx_currency_exchange.backend.domain.exception.InvalidCurrencyConversionAmountException;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Objects;

import static com.fx_currency_exchange.backend.application.constant.ApplicationConstant.*;

@UtilityClass
public class CurrencyValidatorUtil {
    public void validateCurrencyCode(final String currencyCode) {
        if (StringUtils.isBlank(currencyCode) || !currencyCode.matches(CURRENCY_CODE_REGEX)) {
            throw new InvalidCurrencyCodeException(currencyCode);
        }
    }

    public void validateAmount(final BigDecimal amount) {
        if (Objects.isNull(amount) || amount.compareTo(MINIMUM_CONVERSION_AMOUNT) <= ZERO) {
            throw new InvalidCurrencyConversionAmountException(amount);
        }
    }
}