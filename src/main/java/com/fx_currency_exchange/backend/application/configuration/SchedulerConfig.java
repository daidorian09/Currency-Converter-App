package com.fx_currency_exchange.backend.application.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import static com.fx_currency_exchange.backend.application.constant.ApplicationConstant.EXCHANGE_RATE_SCHEDULED_JOB_PREFIX;
import static com.fx_currency_exchange.backend.application.constant.ApplicationConstant.EXCHANGE_RATE_THREAD_POOL_SIZE;

@Configuration
public class SchedulerConfig {

    @Bean
    public TaskScheduler taskScheduler() {
        final ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(EXCHANGE_RATE_THREAD_POOL_SIZE);
        scheduler.setThreadNamePrefix(EXCHANGE_RATE_SCHEDULED_JOB_PREFIX);
        scheduler.initialize();
        return scheduler;
    }
}