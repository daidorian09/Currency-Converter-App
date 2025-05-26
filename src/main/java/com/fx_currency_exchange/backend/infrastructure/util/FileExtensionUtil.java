package com.fx_currency_exchange.backend.infrastructure.util;

import com.fx_currency_exchange.backend.application.constant.ApplicationConstant;
import com.fx_currency_exchange.backend.domain.exception.InvalidUploadedFilenameException;
import com.fx_currency_exchange.backend.domain.exception.UnsupportedFileTypeException;
import lombok.experimental.UtilityClass;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Locale;
import java.util.Objects;

@UtilityClass
public class FileExtensionUtil {
    public static String extractExtension(final String filename) {
        if (StringUtils.isBlank(filename) || !filename.contains(ApplicationConstant.DOT)) {
            throw new UnsupportedFileTypeException(filename);
        }
        return FilenameUtils.getExtension(filename).toLowerCase();
    }

    public static String extractNormalizedFilename(final MultipartFile file) {
        if (Objects.isNull(file) || StringUtils.isBlank(file.getOriginalFilename())) {
            throw new InvalidUploadedFilenameException();
        }
        return file.getOriginalFilename().toLowerCase(Locale.ROOT).trim();
    }
}