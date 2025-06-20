package com.waildevil.job_board_api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.*;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Job Board API")
                        .version("1.0")
                        .description("API documentation for the Job Board platform")
                        .contact(new Contact()
                                .name("WailDevil")
                                .email("wail.homan1@hotmail.com")
                                .url("https://github.com/waildevil/")));
    }
}
