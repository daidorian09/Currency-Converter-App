package com.fx_currency_exchange.backend.application.service.impl;

import com.fx_currency_exchange.backend.application.dto.response.FileUploadConversionResponse;
import com.fx_currency_exchange.backend.application.service.CurrencyConversionService;
import com.fx_currency_exchange.backend.application.service.strategy.parser.CsvFileParser;
import com.fx_currency_exchange.backend.application.service.strategy.parser.FileParserFactory;
import com.fx_currency_exchange.backend.application.service.strategy.parser.FileParserStrategy;
import com.fx_currency_exchange.backend.domain.entity.ConversionTransaction;
import com.fx_currency_exchange.backend.domain.exception.CsvUploadInProgressException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.mock.web.MockMultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class FileUploadConversionServiceImplTest {

    private RedissonClient redissonClient;
    private CurrencyConversionService conversionService;
    private FileParserFactory fileParserFactory;
    private FileUploadConversionConversionServiceImpl service;

    @BeforeEach
    void setUp() {
        redissonClient = mock(RedissonClient.class);
        conversionService = mock(CurrencyConversionService.class);
        fileParserFactory = mock(FileParserFactory.class);
        service = new FileUploadConversionConversionServiceImpl(fileParserFactory, conversionService, redissonClient);
    }

    @Test
    void shouldHandleConcurrentUploadAttempts() throws Exception {
        final MockMultipartFile file = new MockMultipartFile("file", "test.csv", "text/csv", "from,to,amount\nUSD,TRY,100".getBytes());
        final RLock lock = mock(RLock.class);
        final FileParserStrategy parser = new CsvFileParser();

        final AtomicInteger lockAttempt = new AtomicInteger(0);
        when(redissonClient.getLock(anyString())).thenReturn(lock);
        when(fileParserFactory.getParser(file)).thenReturn(parser);

        when(lock.tryLock(anyLong(), anyLong(), any())).thenAnswer(invocation -> lockAttempt.getAndIncrement() == 0);

        final CountDownLatch latch = new CountDownLatch(1);
        final ExecutorService executor = Executors.newFixedThreadPool(2);
        final AtomicReference<Exception> exceptionRef = new AtomicReference<>();

        final Runnable task1 = () -> {
            try {
                latch.await();
                service.process(file);
            } catch (Exception ignored) {
            }
        };

        final Runnable task2 = () -> {
            try {
                latch.await();
                Thread.sleep(3000);
                service.process(file);
            } catch (Exception ex) {
                exceptionRef.set(ex);
            }
        };

        executor.submit(task1);
        executor.submit(task2);

        latch.countDown();
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        assertInstanceOf(CsvUploadInProgressException.class, exceptionRef.get());
    }

    @Test
    void shouldUnlockWhenLockIsAcquired() throws Exception {
        final MockMultipartFile file = new MockMultipartFile("file", "test.csv", "text/csv",
                "from,to,amount\nUSD,TRY,100".getBytes());

        final RLock lock = mock(RLock.class);
        final FileParserStrategy parser = mock(CsvFileParser.class);

        when(redissonClient.getLock(anyString())).thenReturn(lock);
        when(lock.tryLock(anyLong(), anyLong(), any())).thenReturn(true);
        when(fileParserFactory.getParser(file)).thenReturn(parser);
        List<String[]> rows = new ArrayList<>(Collections.singleton(new String[]{"USD", "TRY", "100"}));

        when(parser.parse(file)).thenReturn(rows);

        when(conversionService.convert(any())).thenReturn(mock(ConversionTransaction.class));

        final FileUploadConversionResponse response = service.process(file);

        verify(lock).unlock();
        assertEquals(1, response.successCount());
        assertEquals(0, response.failureCount());
    }

    @Test
    void shouldThrowRuntimeExceptionWhenInterruptedDuringLock() throws InterruptedException {
        final MockMultipartFile file = new MockMultipartFile("file", "test.csv", "text/csv", "from,to,amount\nUSD,TRY,100".getBytes());
        final RLock lock = mock(RLock.class);
        when(redissonClient.getLock(anyString())).thenReturn(lock);

        when(lock.tryLock(anyLong(), anyLong(), any())).thenThrow(new InterruptedException("Thread was interrupted"));

        final RuntimeException exception = assertThrows(RuntimeException.class, () -> service.process(file));
        assertEquals("CSV upload interrupted", exception.getMessage());
        assertInstanceOf(InterruptedException.class, exception.getCause());
    }

    @Test
    void shouldThrowExceptionWhenLockNotAcquired() throws Exception {
        final MockMultipartFile file = new MockMultipartFile("file", "test.csv", "text/csv", "from,to,amount\nUSD,TRY,100".getBytes());
        final RLock lock = mock(RLock.class);

        when(redissonClient.getLock(anyString())).thenReturn(lock);
        when(lock.tryLock(anyLong(), anyLong(), any())).thenReturn(false);

        CsvUploadInProgressException exception = assertThrows(
                CsvUploadInProgressException.class,
                () -> service.process(file)
        );

        assertTrue(exception.getMessage().contains("test.csv"));

        verify(lock, never()).unlock();
    }

    @Test
    void shouldHandleRowProcessingFailureAndCountFailureCorrectly() throws Exception {
        final MockMultipartFile file = new MockMultipartFile("file", "test.csv", "text/csv",
                "from,to,amount\nUSD,TRY,100\nEUR,USD,200".getBytes());

        final RLock lock = mock(RLock.class);
        final FileParserStrategy parser = mock(CsvFileParser.class);

        when(redissonClient.getLock(anyString())).thenReturn(lock);
        when(lock.tryLock(anyLong(), anyLong(), any())).thenReturn(true);
        when(fileParserFactory.getParser(file)).thenReturn(parser);

        final List<String[]> rows = List.of(
                new String[]{"USD", "TRY", "100"},
                new String[]{"EUR", "USD", "200"}
        );
        when(parser.parse(file)).thenReturn(rows);

        doThrow(new RuntimeException("Conversion failed"))
                .when(conversionService).convert(any());

        final FileUploadConversionResponse response = service.process(file);

        assertEquals(0, response.successCount());
        assertEquals(2, response.failureCount());

        verify(conversionService, times(2)).convert(any());
    }
}