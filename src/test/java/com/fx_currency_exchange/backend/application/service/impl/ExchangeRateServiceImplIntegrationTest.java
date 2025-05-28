package com.fx_currency_exchange.backend.application.service.impl;

import com.fx_currency_exchange.backend.application.constant.ApplicationConstant;
import com.fx_currency_exchange.backend.application.dto.request.CreateExchangeRateRequest;
import com.fx_currency_exchange.backend.application.dto.request.ExchangeRateRequest;
import com.fx_currency_exchange.backend.domain.entity.ExchangeRate;
import com.fx_currency_exchange.backend.domain.exception.ExchangeRateNotFound;
import com.fx_currency_exchange.backend.infrastructure.scheduler.ExchangeRateSyncJob;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@EnableCaching
public class ExchangeRateServiceImplIntegrationTest {

    @MockBean
    private ExchangeRateSyncJob exchangeRateSyncJob;

    @Autowired
    private ExchangeRateServiceImpl exchangeRateService;

    @Autowired
    private CacheManager cacheManager;

    @Test
    void getExchangeRate_shouldCacheTheResult() {
        final String from = "USD";
        final String to = "TRY";
        final BigDecimal rateValue = BigDecimal.valueOf(30);

        CreateExchangeRateRequest createRequest = new CreateExchangeRateRequest(from, to, rateValue);
        exchangeRateService.createExchangeRate(createRequest);

        final ExchangeRateRequest exchangeRateRequest = new ExchangeRateRequest(from, to);
        final ExchangeRate existingExchangeRate = exchangeRateService.getExchangeRate(exchangeRateRequest);

        assertThat(existingExchangeRate.getRate()).isEqualByComparingTo(rateValue);

        final String cacheKey = "%s-%s".formatted(from, to);
        final Object cached = cacheManager.getCache(ApplicationConstant.EXCHANGE_RATE_CACHE_NAME).get(cacheKey, ExchangeRate.class);
        assertThat(cached).isNotNull();
        assertThat(((ExchangeRate) cached).getRate()).isEqualByComparingTo(rateValue);
    }

    @Test
    void getExchangeRate_shouldThrowWhenRateNotExists() {
        final ExchangeRateRequest request = new ExchangeRateRequest("ABC", "XYZ");
        assertThrows(ExchangeRateNotFound.class, () -> exchangeRateService.getExchangeRate(request));
    }
}