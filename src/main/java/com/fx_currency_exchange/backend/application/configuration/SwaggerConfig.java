package com.fx_currency_exchange.backend.application.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("FX Currency Exchange API")
                        .description("API documentation for foreign exchange application which is one of the mostcommon services used in financial applications")
                        .version("1.0.0"))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Local Server")
                ));
    }

    @Bean
    public GroupedOpenApi apiControllers() {
        return GroupedOpenApi.builder()
                .group("controllers")
                .pathsToMatch("/api/currency/**")
                .build();
    }
}
