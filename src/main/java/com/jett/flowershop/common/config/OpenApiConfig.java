package com.jett.flowershop.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI flowerShopOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Flower Shop API")
                        .description("REST API for Flower Shop Backend")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Flower Shop Team")
                                .email("support@flowershop.com")));
    }
}
