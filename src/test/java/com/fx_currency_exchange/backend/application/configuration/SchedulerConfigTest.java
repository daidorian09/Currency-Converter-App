package com.fx_currency_exchange.backend.application.configuration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import static com.fx_currency_exchange.backend.application.constant.ApplicationConstant.EXCHANGE_RATE_THREAD_POOL_SIZE;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(SchedulerConfig.class)
class SchedulerConfigTest {

    @Autowired
    private TaskScheduler taskScheduler;

    @Test
    void shouldLoadTaskSchedulerBean() {
        assertThat(taskScheduler).isNotNull();
        assertThat(taskScheduler).isInstanceOf(ThreadPoolTaskScheduler.class);

        final ThreadPoolTaskScheduler scheduler = (ThreadPoolTaskScheduler) taskScheduler;
        assertThat(scheduler.getScheduledThreadPoolExecutor().getCorePoolSize())
                .isEqualTo(EXCHANGE_RATE_THREAD_POOL_SIZE);
    }
}