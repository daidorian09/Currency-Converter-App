package com.fx_currency_exchange.backend.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fx_currency_exchange.backend.application.dto.request.CurrencyConversionRequest;
import com.fx_currency_exchange.backend.application.service.CurrencyConversionService;
import com.fx_currency_exchange.backend.application.service.ExchangeRateService;
import com.fx_currency_exchange.backend.domain.entity.ConversionTransaction;
import com.fx_currency_exchange.backend.domain.entity.ExchangeRate;
import com.fx_currency_exchange.backend.domain.exception.ExchangeRateNotFound;
import com.fx_currency_exchange.backend.domain.exception.SameCurrencyConversionException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CurrencyController.class)
class CurrencyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CurrencyConversionService conversionService;

    @MockBean
    private ExchangeRateService exchangeRateService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnExchangeRateSuccessfully() throws Exception {
        final ExchangeRate rate = ExchangeRate.builder()
                .fromCurrency("USD")
                .toCurrency("TRY")
                .rate(BigDecimal.valueOf(32.50))
                .build();

        Mockito.when(exchangeRateService.getExchangeRate(any()))
                .thenReturn(rate);

        mockMvc.perform(get("/api/currency/rate")
                        .param("fromCurrency", "USD")
                        .param("toCurrency", "TRY"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fromCurrency").value("USD"))
                .andExpect(jsonPath("$.toCurrency").value("TRY"))
                .andExpect(jsonPath("$.rate").value(32.50));
    }

    @Test
    void shouldConvertCurrencySuccessfully() throws Exception {
        final CurrencyConversionRequest request = new CurrencyConversionRequest("USD", "TRY", BigDecimal.valueOf(100));
        final ConversionTransaction tx = ConversionTransaction.builder()
                .id(UUID.randomUUID())
                .fromCurrency("USD")
                .toCurrency("TRY")
                .amount(BigDecimal.valueOf(100))
                .convertedAmount(BigDecimal.valueOf(3250))
                .build();

        Mockito.when(conversionService.convert(any())).thenReturn(tx);

        mockMvc.perform(post("/api/currency/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionId").value(tx.getId().toString()))
                .andExpect(jsonPath("$.convertedAmount").value(3250));
    }

    @Test
    void shouldReturnNotFoundWhenExchangeRateMissing() throws Exception {
        Mockito.when(exchangeRateService.getExchangeRate(any()))
                .thenThrow(new ExchangeRateNotFound("USD", "XXX"));

        mockMvc.perform(get("/api/currency/rate")
                        .param("fromCurrency", "USD")
                        .param("toCurrency", "XXX"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnBadRequestForInvalidConversion() throws Exception {
        CurrencyConversionRequest request = new CurrencyConversionRequest("USD", "USD", BigDecimal.valueOf(100));

        Mockito.when(conversionService.convert(any()))
                .thenThrow(new SameCurrencyConversionException("USD"));

        mockMvc.perform(post("/api/currency/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}