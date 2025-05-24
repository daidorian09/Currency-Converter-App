package com.fx_currency_exchange.backend.api.controller;

import com.fx_currency_exchange.backend.application.dto.request.CurrencyConversionRequest;
import com.fx_currency_exchange.backend.application.dto.request.ExchangeRateRequest;
import com.fx_currency_exchange.backend.application.dto.response.CurrencyConversionResponse;
import com.fx_currency_exchange.backend.application.dto.response.ExchangeRateResponse;
import com.fx_currency_exchange.backend.application.mapper.CurrencyConversionResponseMapper;
import com.fx_currency_exchange.backend.application.mapper.ExchangeRateResponseMapper;
import com.fx_currency_exchange.backend.application.service.CurrencyConversionService;
import com.fx_currency_exchange.backend.application.service.ExchangeRateService;
import com.fx_currency_exchange.backend.domain.entity.ConversionTransaction;
import com.fx_currency_exchange.backend.domain.entity.ExchangeRate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/currency")
@RequiredArgsConstructor
public class CurrencyController {

    private final CurrencyConversionService conversionService;
    private final ExchangeRateService exchangeRateService;

    @Operation(
            summary = "Get exchange rate between two currencies",
            description = "Returns the exchange rate for the given source and target currency codes.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Exchange rate found",
                            content = @Content(schema = @Schema(implementation = ExchangeRateResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Exchange rate not found")
            }
    )
    @GetMapping("/rate")
    public ResponseEntity<ExchangeRateResponse> getExchangeRate(
            @RequestBody final ExchangeRateRequest request
    ) {
        final ExchangeRate exchangeRate = exchangeRateService.getExchangeRate(request);
        return ResponseEntity.ok(ExchangeRateResponseMapper.from(exchangeRate));
    }

    @Operation(
            summary = "Convert currency amount",
            description = "Performs a currency conversion using the current exchange rate, and returns the converted amount and transaction ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Conversion successful",
                            content = @Content(schema = @Schema(implementation = CurrencyConversionResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request")
            }
    )
    @PostMapping("/convert")
    public ResponseEntity<CurrencyConversionResponse> convertCurrency(
            @RequestBody final CurrencyConversionRequest request
    ) {
        final ConversionTransaction transaction = conversionService.convert(request);
        return ResponseEntity.ok(CurrencyConversionResponseMapper.from(transaction));
    }
}