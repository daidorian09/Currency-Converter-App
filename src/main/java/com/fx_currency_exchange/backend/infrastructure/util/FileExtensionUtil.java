package com.fx_currency_exchange.backend.infrastructure.util;

import com.fx_currency_exchange.backend.application.constant.ApplicationConstant;
import com.fx_currency_exchange.backend.domain.exception.UnsupportedFileTypeException;
import lombok.experimental.UtilityClass;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

@UtilityClass
public class FileExtensionUtil {
    public static String extractExtension(final String filename) {
        if (StringUtils.isBlank(filename) || !filename.contains(ApplicationConstant.DOT)) {
            throw new UnsupportedFileTypeException(filename);
        }
        return FilenameUtils.getExtension(filename).toLowerCase();
    }
}