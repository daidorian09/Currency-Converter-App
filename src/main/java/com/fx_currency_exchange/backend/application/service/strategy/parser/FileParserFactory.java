package com.fx_currency_exchange.backend.application.service.strategy.parser;

import com.fx_currency_exchange.backend.domain.exception.UnsupportedFileTypeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.fx_currency_exchange.backend.infrastructure.util.FileExtensionUtil.extractExtension;

@Component
@RequiredArgsConstructor
public class FileParserFactory {

    private final List<FileParserStrategy> strategies;

    public FileParserStrategy getParser(final MultipartFile file) {
        final String extension = extractExtension(file.getOriginalFilename());

        return strategies.stream()
                .filter(strategy -> strategy.supports(extension))
                .findFirst()
                .orElseThrow(() ->
                        new UnsupportedFileTypeException(extension));
    }
}
