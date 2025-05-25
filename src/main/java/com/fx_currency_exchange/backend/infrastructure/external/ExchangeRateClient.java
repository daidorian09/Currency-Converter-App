package com.fx_currency_exchange.backend.infrastructure.external;

import java.math.BigDecimal;

public interface ExchangeRateClient {
    BigDecimal getRate(final String from, final String to);
}
