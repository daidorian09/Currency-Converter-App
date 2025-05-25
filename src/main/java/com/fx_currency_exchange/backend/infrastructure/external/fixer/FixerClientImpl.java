package com.fx_currency_exchange.backend.infrastructure.external.fixer;

import com.fx_currency_exchange.backend.application.configuration.FixerConfig;
import com.fx_currency_exchange.backend.infrastructure.external.ExchangeRateClient;
import com.fx_currency_exchange.backend.infrastructure.external.dto.response.FixerResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class FixerClientImpl implements ExchangeRateClient {

    private static final int SCALE = 6;
    private final RestTemplate restTemplate;
    private final FixerConfig fixerConfig;

    @Override
    public BigDecimal getRate(String fromCurrency, String toCurrency) {
        log.info("Fetching exchange rates from Fixer for {} to {}", fromCurrency, toCurrency);

        final String url = String.format("%s/latest?access_key=%s&symbols=%s,%s",
                fixerConfig.getBaseUrl(),
                fixerConfig.getApiKey(),
                fromCurrency,
                toCurrency);

        final FixerResponse response = restTemplate.getForObject(url, FixerResponse.class);

        BigDecimal crossRate = extractCrossRate(fromCurrency, toCurrency, response);

        log.info("Calculated cross rate ({} -> {}): {}", fromCurrency, toCurrency, crossRate);
        return crossRate;
    }

    private static BigDecimal extractCrossRate(String fromCurrency, String toCurrency, FixerResponse response) {
        if (Objects.isNull(response) || !response.success()) {
            throw new RuntimeException("Failed to fetch exchange rates from Fixer.");
        }

        final Map<String, BigDecimal> rates = response.rates();
        final BigDecimal fromRate = rates.get(fromCurrency);
        final BigDecimal toRate = rates.get(toCurrency);

        if (Objects.isNull(fromRate) || Objects.isNull(toRate)) {
            throw new RuntimeException("Missing currency rates in Fixer response for " + fromCurrency + " or " + toCurrency);
        }

        return toRate.divide(fromRate, SCALE, RoundingMode.HALF_UP);
    }
}