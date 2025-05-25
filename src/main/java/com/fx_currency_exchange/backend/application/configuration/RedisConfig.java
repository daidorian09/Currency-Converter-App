package com.fx_currency_exchange.backend.application.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "redis")
public class RedisConfig {
    private String host;
    private int port;
    private int connectionPoolSize;
    private int connectionMinimumIdleSize;
    private int timeout;
    private int retryCount;
    private int retryInterval;
}