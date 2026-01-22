package com.ecomerce.process.order.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI carDatabaseOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("E-Commerce Order Process API")
                        .description("API for managing orders in an e-commerce system")
                        .version("1.0.0"));
    }
}
