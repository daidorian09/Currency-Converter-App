package com.fx_currency_exchange.backend.application.configuration;

import lombok.Getter;
import lombok.Setter;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.util.TimeValue;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

import static com.fx_currency_exchange.backend.application.constant.ApplicationConstant.*;

@Configuration
public class RestTemplateConfig {

    @Bean
    @ConfigurationProperties(prefix = "resttemplate")
    public RestTemplateProperties restTemplateProperties() {
        return new RestTemplateProperties();
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateProperties props) {
        final PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(props.getMaxTotalConnections());
        connectionManager.setDefaultMaxPerRoute(props.getMaxPerRoute());

        final CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .evictIdleConnections(TimeValue.of(Duration.ofSeconds(props.getIdleConnectionEvictSeconds())))
                .build();

        final HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
        factory.setConnectTimeout(props.getConnectTimeout());
        factory.setReadTimeout(props.getReadTimeout());
        factory.setConnectionRequestTimeout(props.getConnectionRequestTimeout());

        return new RestTemplate(factory);
    }

    @Getter
    @Setter
    public static class RestTemplateProperties {
        private int maxTotalConnections = MAX_TOTAL_CONNECTION;
        private int maxPerRoute = MAX_PER_ROUTE;
        private int connectTimeout = CONNECTION_TIMEOUT;
        private int readTimeout = READ_TIMEOUT;
        private int connectionRequestTimeout = CONNECTION_REQUEST_TIMEOUT;
        private int idleConnectionEvictSeconds = IDLE_CONNECTION_EVICT_SECONDS;
    }
}