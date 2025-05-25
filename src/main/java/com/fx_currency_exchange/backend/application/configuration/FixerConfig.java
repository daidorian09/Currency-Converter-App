package com.fx_currency_exchange.backend.application.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "external.fixer")
public class FixerConfig {
    private String apiKey;
    private String baseUrl;
}