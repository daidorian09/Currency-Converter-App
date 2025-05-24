package com.fx_currency_exchange.backend.application.service.strategy.parser;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public sealed interface FileParserStrategy permits FileParser {

    boolean supports(final String extension);

    List<String[]> parse(MultipartFile file);
}
