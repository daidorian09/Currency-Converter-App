package com.fx_currency_exchange.backend.application.service.impl;

import com.fx_currency_exchange.backend.application.dto.request.CurrencyConversionRequest;
import com.fx_currency_exchange.backend.application.service.CurrencyConversionService;
import com.fx_currency_exchange.backend.domain.entity.ConversionTransaction;
import com.fx_currency_exchange.backend.domain.entity.ExchangeRate;
import com.fx_currency_exchange.backend.domain.exception.ExchangeRateNotFound;
import com.fx_currency_exchange.backend.domain.exception.SameCurrencyConversionException;
import com.fx_currency_exchange.backend.domain.service.ConversionTransactionRepository;
import com.fx_currency_exchange.backend.domain.service.ExchangeRateRepository;
import com.fx_currency_exchange.backend.infrastructure.util.CurrencyValidatorUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrencyConversionServiceImpl implements CurrencyConversionService {

    private final ExchangeRateRepository exchangeRateRepository;
    private final ConversionTransactionRepository transactionRepository;

    @Override
    public ConversionTransaction convert(final CurrencyConversionRequest request) {
        validateRequest(request);

        final ExchangeRate rate = exchangeRateRepository.findByCurrencyPair(request.fromCurrency(), request.toCurrency())
                .orElseThrow(() -> new ExchangeRateNotFound(request.fromCurrency(), request.toCurrency()));

        final ConversionTransaction transaction = ConversionTransaction.builder()
                .fromCurrency(request.fromCurrency())
                .toCurrency(request.toCurrency())
                .amount(request.amount())
                .convertedAmount(request.amount().multiply(rate.getRate()))
                .build();

        transactionRepository.save(transaction);
        return transaction;
    }

    private static void validateRequest(CurrencyConversionRequest request) {
        CurrencyValidatorUtil.validateCurrencyCode(request.fromCurrency());
        CurrencyValidatorUtil.validateCurrencyCode(request.toCurrency());
        CurrencyValidatorUtil.validateAmount(request.amount());

        if (StringUtils.equalsIgnoreCase(request.fromCurrency(), request.toCurrency())) {
            throw new SameCurrencyConversionException(request.fromCurrency());
        }
    }
}