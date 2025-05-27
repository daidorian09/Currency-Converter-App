package com.fx_currency_exchange.backend.infrastructure.external.fixer;

import com.fx_currency_exchange.backend.application.configuration.FixerConfig;
import com.fx_currency_exchange.backend.domain.exception.FixerRateUnavailableException;
import com.fx_currency_exchange.backend.infrastructure.external.dto.response.FixerResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FixerClientImplTest {
    @Mock
    private RestTemplate restTemplate;

    @Mock
    private FixerConfig fixerConfig;

    @InjectMocks
    private FixerClientImpl fixerClient;

    @Test
    void shouldReturnCrossRate_whenFixerReturnsValidResponse() {
        final String from = "EUR";
        final String to = "TRY";
        final String baseUrl = "https://data.fixer.io/api";
        final String apiKey = "dummy-key";
        final String url = String.format("%s/latest?access_key=%s&symbols=%s,%s", baseUrl, apiKey, from, to);

        FixerResponse response = new FixerResponse(Boolean.TRUE,
                Instant.now().toEpochMilli(),
                from,
                Instant.now().toString(),
                Map.of(
                        "EUR", BigDecimal.ONE,
                        "TRY", new BigDecimal("30.123456")
                ));

        when(fixerConfig.getBaseUrl()).thenReturn(baseUrl);
        when(fixerConfig.getApiKey()).thenReturn(apiKey);
        when(restTemplate.getForObject(url, FixerResponse.class)).thenReturn(response);

        BigDecimal rate = fixerClient.getRate(from, to);

        assertEquals(new BigDecimal("30.123456"), rate);
    }

    @Test
    void shouldThrowException_whenFixerResponseIsNull() {
        when(restTemplate.getForObject(anyString(), eq(FixerResponse.class)))
                .thenReturn(null);

        assertThrows(FixerRateUnavailableException.class, () -> fixerClient.getRate("USD", "TRY"));
    }

    @Test
    void shouldThrowException_whenFixerResponseIsNotSuccessful() {
        final FixerResponse response = new FixerResponse(Boolean.FALSE,
                Instant.now().toEpochMilli(),
                "EUR",
                Instant.now().toString(),
                Map.of(
                        "EUR", BigDecimal.ONE,
                        "TRY", new BigDecimal("30.123456")
                ));

        when(restTemplate.getForObject(anyString(), eq(FixerResponse.class)))
                .thenReturn(response);

        assertThrows(FixerRateUnavailableException.class, () -> fixerClient.getRate("USD", "TRY"));
    }

    @Test
    void shouldThrowWhenFromRateIsMissing() {
        final Map<String, BigDecimal> rates = Map.of("TRY", new BigDecimal("30.123456"));

        final FixerResponse response = new FixerResponse(
                true,
                System.currentTimeMillis(),
                "EUR",
                LocalDate.now().toString(),
                rates
        );


        when(restTemplate.getForObject(anyString(), eq(FixerResponse.class)))
                .thenReturn(response);

        assertThrows(FixerRateUnavailableException.class, () ->
                fixerClient.getRate("USD", "TRY")
        );
    }

    @Test
    void shouldThrowWhenToRateIsMissing() {
        final Map<String, BigDecimal> rates = Map.of("USD", BigDecimal.ONE);

        final FixerResponse response = new FixerResponse(
                true,
                System.currentTimeMillis(),
                "EUR",
                LocalDate.now().toString(),
                rates
        );

        when(restTemplate.getForObject(anyString(), eq(FixerResponse.class)))
                .thenReturn(response);

        assertThrows(FixerRateUnavailableException.class, () ->
                fixerClient.getRate("USD", "TRY")
        );
    }

    @Test
    void shouldThrowWhenBothRatesAreMissing() {
        final FixerResponse response = new FixerResponse(
                true,
                System.currentTimeMillis(),
                "EUR",
                LocalDate.now().toString(),
                Collections.emptyMap()
        );

        when(restTemplate.getForObject(anyString(), eq(FixerResponse.class)))
                .thenReturn(response);

        assertThrows(FixerRateUnavailableException.class, () ->
                fixerClient.getRate("USD", "TRY")
        );
    }
}