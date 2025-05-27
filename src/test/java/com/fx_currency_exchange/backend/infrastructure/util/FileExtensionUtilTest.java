package com.fx_currency_exchange.backend.infrastructure.util;

import com.fx_currency_exchange.backend.domain.exception.InvalidUploadedFilenameException;
import com.fx_currency_exchange.backend.domain.exception.UnsupportedFileTypeException;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FileExtensionUtilTest {
    @Test
    void shouldExtractExtensionSuccessfully() {
        final String filename = "test_file.csv";
        final String extension = FileExtensionUtil.extractExtension(filename);
        assertEquals("csv", extension);
    }

    @Test
    void shouldThrowWhenFilenameIsBlankOrMissingDot() {
        assertThrows(UnsupportedFileTypeException.class, () -> FileExtensionUtil.extractExtension(null));
        assertThrows(UnsupportedFileTypeException.class, () -> FileExtensionUtil.extractExtension(""));
        assertThrows(UnsupportedFileTypeException.class, () -> FileExtensionUtil.extractExtension("invalidfilename"));
    }

    @Test
    void shouldExtractNormalizedFilenameSuccessfully() {
        final MockMultipartFile file = new MockMultipartFile("file", "MyFile.CSV", "text/csv", new byte[0]);
        final String normalized = FileExtensionUtil.extractNormalizedFilename(file);
        assertEquals("myfile.csv", normalized);
    }

    @Test
    void shouldThrowWhenOriginalFilenameIsNullOrBlank() {
        final MockMultipartFile fileWithNullName = new MockMultipartFile("file", null, "text/csv", new byte[0]);
        final MockMultipartFile fileWithEmptyName = new MockMultipartFile("file", "", "text/csv", new byte[0]);

        assertThrows(InvalidUploadedFilenameException.class, () -> FileExtensionUtil.extractNormalizedFilename(null));
        assertThrows(InvalidUploadedFilenameException.class, () -> FileExtensionUtil.extractNormalizedFilename(fileWithNullName));
        assertThrows(InvalidUploadedFilenameException.class, () -> FileExtensionUtil.extractNormalizedFilename(fileWithEmptyName));
    }
}