package com.fx_currency_exchange.backend.application.service.strategy.parser;

import com.fx_currency_exchange.backend.application.constant.ApplicationConstant;
import com.fx_currency_exchange.backend.domain.enums.FileType;
import com.fx_currency_exchange.backend.domain.exception.CsvParsingException;
import com.fx_currency_exchange.backend.domain.exception.InvalidCSVFormatException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.IntStream;

import static com.fx_currency_exchange.backend.application.constant.ApplicationConstant.CSV_FILE_DELIMITER;

@Component
@Slf4j
public non-sealed class CsvFileParser extends AbstractFileParser implements FileParserStrategy {

    private static final int HEADER_ROW = 1;
    private static final int START_INCLUSIVE = 0;

    @Override
    public boolean supports(final String extension) {
        return StringUtils.equalsIgnoreCase(extension, FileType.CSV.toString());
    }

    @Override
    public List<String[]> parse(final MultipartFile file) {
        validateFileNotEmpty(file);

        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            return reader.lines()
                    .skip(HEADER_ROW)
                    .map(this::parseLine)
                    .toList();
        } catch (IOException e) {
            log.error("Error while parsing csv file : %s, {}", file.getOriginalFilename(), e);
            throw new CsvParsingException(file.getOriginalFilename());
        }
    }

    private String[] parseLine(final String line) {
        final String[] parts = line.split(CSV_FILE_DELIMITER);

        if (ObjectUtils.notEqual(parts.length, ApplicationConstant.EXPECTED_CSV_COLUMN_COUNT)) {
            throw new InvalidCSVFormatException(parts.length, line);
        }

        IntStream
                .range(START_INCLUSIVE, parts.length)
                .forEach(i -> parts[i] = parts[i].trim());

        return parts;
    }
}