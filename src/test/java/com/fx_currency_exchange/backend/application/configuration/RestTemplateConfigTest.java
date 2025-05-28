package com.fx_currency_exchange.backend.application.configuration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(properties = {
        "resttemplate.max-total-connections=100",
        "resttemplate.max-per-route=20",
        "resttemplate.connect-timeout=2000",
        "resttemplate.read-timeout=3000",
        "resttemplate.connection-request-timeout=1000",
        "resttemplate.idle-connection-evict-seconds=30"
})
@Import(RestTemplateConfig.class)
class RestTemplateConfigTest {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RestTemplateConfig.RestTemplateProperties props;

    @Test
    void shouldLoadRestTemplateBeanAndPropertiesCorrectly() {
        assertThat(restTemplate).isNotNull();
        assertThat(props.getMaxTotalConnections()).isEqualTo(100);
        assertThat(props.getMaxPerRoute()).isEqualTo(20);
        assertThat(props.getConnectTimeout()).isEqualTo(2000);
        assertThat(props.getReadTimeout()).isEqualTo(3000);
        assertThat(props.getConnectionRequestTimeout()).isEqualTo(1000);
        assertThat(props.getIdleConnectionEvictSeconds()).isEqualTo(30);
    }
}