package com.fx_currency_exchange.backend.application.service.impl;

import com.fx_currency_exchange.backend.application.dto.request.CurrencyConversionRequest;
import com.fx_currency_exchange.backend.application.dto.response.FileUploadConversionResponse;
import com.fx_currency_exchange.backend.application.service.FileUploadConversionService;
import com.fx_currency_exchange.backend.application.service.CurrencyConversionService;
import com.fx_currency_exchange.backend.application.service.strategy.parser.FileParserFactory;
import com.fx_currency_exchange.backend.application.service.strategy.parser.FileParserStrategy;
import com.fx_currency_exchange.backend.domain.exception.CsvUploadInProgressException;
import com.fx_currency_exchange.backend.infrastructure.util.FileExtensionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.fx_currency_exchange.backend.application.constant.ApplicationConstant.CSV_UPLOAD_LOCK_KEY_PREFIX;
import static com.fx_currency_exchange.backend.application.constant.ApplicationConstant.CSV_UPLOAD_LOCK_LEASE_TIME;
import static com.fx_currency_exchange.backend.application.constant.ApplicationConstant.CSV_UPLOAD_LOCK_WAIT_TIME;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileUploadConversionConversionServiceImpl implements FileUploadConversionService {

    private final FileParserFactory fileParserFactory;
    private final CurrencyConversionService conversionService;
    private final RedissonClient redissonClient;

    @Override
    public FileUploadConversionResponse process(final MultipartFile file) {
        final String lockKey = "%s%s".formatted(CSV_UPLOAD_LOCK_KEY_PREFIX, FileExtensionUtil.extractNormalizedFilename(file));
        final RLock lock = redissonClient.getLock(lockKey);
        boolean acquired = false;
        try {

            acquired = lock.tryLock(CSV_UPLOAD_LOCK_WAIT_TIME, CSV_UPLOAD_LOCK_LEASE_TIME, TimeUnit.SECONDS);
            if (!acquired) {
                throw new CsvUploadInProgressException(file.getOriginalFilename());
            }

            return processFile(file);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("CSV upload interrupted", e);
        } finally {
            if (acquired) {
                lock.unlock();
                log.info("CSV upload lock released for file: {}", file.getOriginalFilename());
            }
        }
    }

    private FileUploadConversionResponse processFile(final MultipartFile file) {
        log.info("Starting CSV upload processing for file: {}", file.getOriginalFilename());

        final FileParserStrategy parser = fileParserFactory.getParser(file);
        final List<String[]> rows = List.copyOf(parser.parse(file));

        log.info("Parsed {} rows from file: {}", rows.size(), file.getOriginalFilename());

        int success = 0;
        int failure = 0;

        for (final String[] row : rows) {
            try {
                conversionService.convert(new CurrencyConversionRequest(
                        row[0],
                        row[1],
                        new BigDecimal(row[2])));
                success++;
            } catch (Exception e) {
                log.warn("Failed to process row: {}", row, e);
                failure++;
            }
        }

        log.info("CSV upload processing completed for file: {}. Success: {}, Failure: {}", file.getOriginalFilename(), success, failure);

        return new FileUploadConversionResponse(success, failure);
    }
}
