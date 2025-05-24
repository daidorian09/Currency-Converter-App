package com.fx_currency_exchange.backend.application.service.impl;

import com.fx_currency_exchange.backend.application.dto.request.CurrencyConversionRequest;
import com.fx_currency_exchange.backend.application.dto.response.CsvUploadResponse;
import com.fx_currency_exchange.backend.application.service.CsvConversionUploadService;
import com.fx_currency_exchange.backend.application.service.CurrencyConversionService;
import com.fx_currency_exchange.backend.application.service.strategy.parser.FileParserFactory;
import com.fx_currency_exchange.backend.application.service.strategy.parser.FileParserStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CsvConversionUploadServiceImpl implements CsvConversionUploadService {

    private final FileParserFactory fileParserFactory;
    private final CurrencyConversionService conversionService;

    @Override
    public CsvUploadResponse process(final MultipartFile file) {
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

        return new CsvUploadResponse(success, failure);
    }
}
