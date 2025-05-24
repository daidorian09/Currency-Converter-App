package com.fx_currency_exchange.backend.application.service;

import com.fx_currency_exchange.backend.application.dto.response.CsvUploadResponse;
import org.springframework.web.multipart.MultipartFile;

public interface CsvConversionUploadService {
    CsvUploadResponse process(final MultipartFile file);
}
