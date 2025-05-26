package com.fx_currency_exchange.backend.application.service;

import com.fx_currency_exchange.backend.application.dto.response.FileUploadConversionResponse;
import org.springframework.web.multipart.MultipartFile;

public interface FileUploadConversionService {
    FileUploadConversionResponse process(final MultipartFile file);
}
