package com.fx_currency_exchange.backend.infrastructure.scheduler;

import com.fx_currency_exchange.backend.application.configuration.ExchangeRateJobConfig;
import com.fx_currency_exchange.backend.application.dto.request.CreateExchangeRateRequest;
import com.fx_currency_exchange.backend.application.service.ExchangeRateService;
import com.fx_currency_exchange.backend.domain.entity.ExchangeRate;
import com.fx_currency_exchange.backend.infrastructure.external.ExchangeRateClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExchangeRateSyncJobTest {
    @Mock
    private ExchangeRateClient exchangeRateClient;

    @Mock
    private ExchangeRateJobConfig exchangeRateJobConfig;

    @Mock
    private ExchangeRateService exchangeRateService;

    @Mock
    private TaskScheduler taskScheduler;

    @Mock
    private ScheduledFuture scheduledFuture;


    @InjectMocks
    private ExchangeRateSyncJob exchangeRateSyncJob;

    private static final String[][] pairs = {{"USD", "TRY"}, {"EUR", "USD"}};

    @Test
    void shouldScheduleJobAndTriggerInitialSync() {
        when(exchangeRateJobConfig.getCron()).thenReturn("0 */10 * * * *");
        when(exchangeRateJobConfig.getCurrencyPairs()).thenReturn(List.of(pairs[0], pairs[1]));
        when(taskScheduler.schedule(any(Runnable.class), any(CronTrigger.class)))
                .thenReturn(scheduledFuture);
        when(exchangeRateClient.getRate("USD", "TRY")).thenReturn(new BigDecimal("32.5"));

        // when
        exchangeRateSyncJob.scheduleJob();

        // then
        verify(exchangeRateClient).getRate("USD", "TRY");
        verify(exchangeRateService, times(2)).createExchangeRate(any());
        verify(exchangeRateService, times(2)).updateCacheableExchangeRate(any());
        verify(taskScheduler).schedule(any(Runnable.class), any(CronTrigger.class));
    }

    @Test
    void shouldSyncExchangeRatesSuccessfully() {
        when(exchangeRateJobConfig.getCurrencyPairs()).thenReturn(List.of(pairs[0], pairs[1]));
        when(exchangeRateClient.getRate("USD", "TRY")).thenReturn(BigDecimal.valueOf(27.3));
        when(exchangeRateClient.getRate("EUR", "USD")).thenReturn(BigDecimal.valueOf(1.1));

        final CreateExchangeRateRequest req1 = new CreateExchangeRateRequest("USD", "TRY", BigDecimal.valueOf(27.3));
        final CreateExchangeRateRequest req2 = new CreateExchangeRateRequest("EUR", "USD", BigDecimal.valueOf(1.1));

        final ExchangeRate rate1 = ExchangeRate.builder().fromCurrency("USD").toCurrency("TRY").rate(BigDecimal.valueOf(27.3)).build();
        final ExchangeRate rate2 = ExchangeRate.builder().fromCurrency("EUR").toCurrency("USD").rate(BigDecimal.valueOf(1.1)).build();

        when(exchangeRateService.createExchangeRate(eq(req1))).thenReturn(rate1);
        when(exchangeRateService.createExchangeRate(eq(req2))).thenReturn(rate2);

        exchangeRateSyncJob.syncExchangeRates();

        verify(exchangeRateClient).getRate("USD", "TRY");
        verify(exchangeRateClient).getRate("EUR", "USD");

        verify(exchangeRateService).createExchangeRate(eq(req1));
        verify(exchangeRateService).createExchangeRate(eq(req2));

        verify(exchangeRateService).updateCacheableExchangeRate(rate1);
        verify(exchangeRateService).updateCacheableExchangeRate(rate2);
    }

    @Test
    void shouldLogWarningWhenRateFetchingFails() {
        when(exchangeRateJobConfig.getCurrencyPairs()).thenReturn(List.of(pairs[0], pairs[1]));

        when(exchangeRateClient.getRate("USD", "TRY")).thenReturn(BigDecimal.valueOf(27.3));
        when(exchangeRateClient.getRate("EUR", "USD")).thenThrow(new RuntimeException("External service unavailable"));

        final ExchangeRate rate1 = ExchangeRate.builder().fromCurrency("USD").toCurrency("TRY").rate(BigDecimal.valueOf(27.3)).build();
        final CreateExchangeRateRequest req1 = new CreateExchangeRateRequest("USD", "TRY", BigDecimal.valueOf(27.3));
        when(exchangeRateService.createExchangeRate(req1)).thenReturn(rate1);

        exchangeRateSyncJob.syncExchangeRates();

        verify(exchangeRateClient).getRate("USD", "TRY");
        verify(exchangeRateClient).getRate("EUR", "USD");
        verify(exchangeRateService).createExchangeRate(req1);
        verify(exchangeRateService).updateCacheableExchangeRate(rate1);

        verify(exchangeRateService, never()).createExchangeRate(argThat(req ->
                "EUR".equals(req.fromCurrency()) && "USD".equals(req.toCurrency())));
    }

    @Test
    void shouldNotSyncWhenCurrencyPairsAreEmpty() {
        when(exchangeRateJobConfig.getCurrencyPairs()).thenReturn(Collections.emptyList());

        exchangeRateSyncJob.syncExchangeRates();

        verifyNoInteractions(exchangeRateClient);
        verifyNoInteractions(exchangeRateService);
    }
}