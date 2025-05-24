package com.fx_currency_exchange.backend.application.service;

import com.fx_currency_exchange.backend.application.dto.request.CurrencyConversionRequest;
import com.fx_currency_exchange.backend.domain.entity.ConversionTransaction;

public interface CurrencyConversionService {
    ConversionTransaction convert(final CurrencyConversionRequest request);
}
