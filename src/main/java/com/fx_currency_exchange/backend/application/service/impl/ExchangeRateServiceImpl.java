package com.fx_currency_exchange.backend.application.service.impl;

import com.fx_currency_exchange.backend.application.dto.request.CreateExchangeRateRequest;
import com.fx_currency_exchange.backend.application.dto.request.ExchangeRateRequest;
import com.fx_currency_exchange.backend.application.service.ExchangeRateService;
import com.fx_currency_exchange.backend.domain.entity.ExchangeRate;
import com.fx_currency_exchange.backend.domain.exception.ExchangeRateNotFound;
import com.fx_currency_exchange.backend.domain.service.ExchangeRateRepository;
import com.fx_currency_exchange.backend.infrastructure.util.CurrencyValidatorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;

import static com.fx_currency_exchange.backend.application.constant.ApplicationConstant.EXCHANGE_RATE_CACHE_NAME;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExchangeRateServiceImpl implements ExchangeRateService {

    private final ExchangeRateRepository exchangeRateRepository;

    @Override
    @Cacheable(value = EXCHANGE_RATE_CACHE_NAME, key = "#request.fromCurrency() + '-' + #request.toCurrency()")
    public ExchangeRate getExchangeRate(final ExchangeRateRequest request) {
        validateRequest(request);

        return exchangeRateRepository
                .findByCurrencyPair(request.fromCurrency(), request.toCurrency())
                .orElseThrow(() -> new ExchangeRateNotFound(request.fromCurrency(), request.toCurrency()));
    }

    @CachePut(value = EXCHANGE_RATE_CACHE_NAME, key = "#rate.fromCurrency + '-' + #rate.toCurrency")
    public ExchangeRate updateCacheableExchangeRate(ExchangeRate rate) {
        log.info("Cache updated for: {}-{}", rate.getFromCurrency(), rate.getToCurrency());
        return rate;
    }

    @Override
    public ExchangeRate createExchangeRate(CreateExchangeRateRequest request) {
        final ExchangeRate exchangeRate = ExchangeRate.builder()
                .fromCurrency(request.fromCurrency())
                .toCurrency(request.toCurrency())
                .rate(request.rate())
                .build();

        exchangeRateRepository.save(exchangeRate);

        return exchangeRate;
    }

    private static void validateRequest(final ExchangeRateRequest request) {
        CurrencyValidatorUtil.validateCurrencyCode(request.fromCurrency());
        CurrencyValidatorUtil.validateCurrencyCode(request.toCurrency());
    }
}