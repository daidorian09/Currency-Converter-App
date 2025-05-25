package com.fx_currency_exchange.backend.application.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

import static com.fx_currency_exchange.backend.application.constant.ApplicationConstant.EXCHANGE_SYNC_JOB_CRON_EXPRESSION;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "exchange.job")
public class ExchangeRateJobConfig {

    private List<String[]> currencyPairs = new ArrayList<>();
    private String cron = EXCHANGE_SYNC_JOB_CRON_EXPRESSION;
}