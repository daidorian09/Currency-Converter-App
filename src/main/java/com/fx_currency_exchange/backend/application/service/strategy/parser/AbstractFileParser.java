package com.fx_currency_exchange.backend.application.service.strategy.parser;

import com.fx_currency_exchange.backend.domain.exception.UploadedFileEmptyException;
import org.springframework.web.multipart.MultipartFile;

public abstract class AbstractFileParser {

    protected void validateFileNotEmpty(final MultipartFile file) {
        if (file.isEmpty()) {
            throw new UploadedFileEmptyException(file.getOriginalFilename());
        }
    }

}