package com.fx_currency_exchange.backend.infrastructure.scheduler;

import com.fx_currency_exchange.backend.application.configuration.ExchangeRateJobConfig;
import com.fx_currency_exchange.backend.domain.entity.ExchangeRate;
import com.fx_currency_exchange.backend.domain.service.ExchangeRateRepository;
import com.fx_currency_exchange.backend.infrastructure.external.ExchangeRateClient;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExchangeRateSyncJob {

    private final ExchangeRateClient exchangeRateClient;
    private final ExchangeRateRepository exchangeRateRepository;
    private final ExchangeRateJobConfig exchangeRateJobConfig;

    private final TaskScheduler taskScheduler;
    private ScheduledFuture<?> scheduledTask;

    @PostConstruct
    public void scheduleJob() {
        log.info("[ExchangeRateSyncJob] Scheduling with cron: {}", exchangeRateJobConfig.getCron());

        this.syncExchangeRates();

        scheduledTask = taskScheduler.schedule(
                this::syncExchangeRates,
                new CronTrigger(exchangeRateJobConfig.getCron())
        );
    }

    public void syncExchangeRates() {
        log.info("[ExchangeRateSyncJob] Starting scheduled sync...");

        final List<String[]> pairs = exchangeRateJobConfig.getCurrencyPairs();

        pairs.forEach(pair -> {
            final String from = pair[0];
            final String to = pair[1];
            try {
                final BigDecimal rate = exchangeRateClient.getRate(from, to);
                final ExchangeRate exchangeRate = ExchangeRate.builder()
                        .fromCurrency(from)
                        .toCurrency(to)
                        .rate(rate)
                        .build();
                exchangeRateRepository.save(exchangeRate);
                log.info("[ExchangeRateSyncJob] Updated: {} -> {} = {}", from, to, rate);
            } catch (Exception e) {
                log.warn("[ExchangeRateSyncJob] Failed to update rate for {} -> {}", from, to, e);
            }
        });
    }
}