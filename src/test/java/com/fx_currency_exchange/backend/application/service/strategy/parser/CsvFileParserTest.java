package com.fx_currency_exchange.backend.application.service.strategy.parser;

import com.fx_currency_exchange.backend.domain.exception.CsvParsingException;
import com.fx_currency_exchange.backend.domain.exception.InvalidCSVFormatException;
import com.fx_currency_exchange.backend.domain.exception.UploadedFileEmptyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CsvFileParserTest {
    private CsvFileParser csvFileParser;

    @BeforeEach
    void setUp() {
        csvFileParser = new CsvFileParser();
    }

    @Test
    void shouldSupportCsvExtension() {
        assertTrue(csvFileParser.supports("csv"));
        assertTrue(csvFileParser.supports("CSV"));
        assertFalse(csvFileParser.supports("json"));
    }

    @Test
    void shouldParseValidCsvFile() {
        final String content = "from,to,amount\nUSD,TRY,100\nEUR,USD,200";
        final MultipartFile file = new MockMultipartFile("file", "test.csv", "text/csv", content.getBytes());

        final List<String[]> result = csvFileParser.parse(file);

        assertEquals(2, result.size());
        assertArrayEquals(new String[]{"USD", "TRY", "100"}, result.get(0));
        assertArrayEquals(new String[]{"EUR", "USD", "200"}, result.get(1));
    }

    @Test
    void shouldThrowExceptionWhenColumnCountIsInvalid() {
        final String content = "from,to,amount\nUSD,TRY";
        MultipartFile file = new MockMultipartFile("file", "test.csv", "text/csv", content.getBytes());

        assertThrows(InvalidCSVFormatException.class, () -> csvFileParser.parse(file));
    }

    @Test
    void shouldThrowExceptionWhenFileIsEmpty() {
        final MultipartFile file = new MockMultipartFile("file", "empty.csv", "text/csv", new byte[0]);

        assertThrows(UploadedFileEmptyException.class, () -> csvFileParser.parse(file));
    }

    @Test
    void shouldThrowCsvParsingExceptionOnIOException() throws IOException {
        final MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("corrupt.csv");
        when(file.getInputStream()).thenThrow(new IOException("error"));

        assertThrows(CsvParsingException.class, () -> csvFileParser.parse(file));
    }
}