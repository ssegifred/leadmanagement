package com.pandolar.leadmanagement.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI leadManagementOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Lead Management API")
                        .description("A CRM module for managing leads and converting them into customers. "
                                + "Supports full CRUD operations on leads, lead-to-customer conversion with "
                                + "duplicate prevention, and customer retrieval.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("PANDOLLAR")
                                .email("info@pandollar.com")));
    }
}
