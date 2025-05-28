package com.fx_currency_exchange.backend.application.service.strategy.parser;

import com.fx_currency_exchange.backend.domain.exception.UnsupportedFileTypeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FileParserFactoryTest {
    @Mock
    private CsvFileParser csvStrategy;

    private FileParserFactory factory;

    @BeforeEach
    void setUp() {
        factory = new FileParserFactory(Collections.singletonList(csvStrategy));
    }

    @Test
    void getParser_supportedExtension_returnsMatchingStrategy() {
        final String filename = "rates.csv";
        final MockMultipartFile file =
                new MockMultipartFile("file", filename, "text/csv", new byte[0]);

        when(csvStrategy.supports("csv")).thenReturn(true);

        final FileParserStrategy result = factory.getParser(file);

        assertThat(result).isSameAs(csvStrategy);
        verify(csvStrategy).supports("csv");
    }

    @Test
    void getParser_unsupportedExtension_throwsException() {
        final String filename = "rates.xlsx";
        final MockMultipartFile file =
                new MockMultipartFile("file", filename, "application/vnd.ms-excel", new byte[0]);

        when(csvStrategy.supports("xlsx")).thenReturn(false);

        assertThrows(UnsupportedFileTypeException.class, () -> factory.getParser(file));
    }
}