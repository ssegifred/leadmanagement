package com.pandolar.leadmanagement.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI leadManagementOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Lead Management API")
                        .description("""
                                A CRM module for managing leads and converting them into customers.

                                **Key Features:**
                                - Full CRUD operations on leads (Create, Read, Update)
                                - Lead-to-customer conversion with duplicate prevention
                                - Customer retrieval (read-only, created via conversion)

                                **Lead Statuses:** NEW → CONTACTED → QUALIFIED → CONVERTED

                                **Business Rules:**
                                - A lead can only be converted to a customer **once**
                                - The CONVERTED status is set automatically via the conversion endpoint
                                - Email addresses must be unique across all leads
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("PANDOLLAR")
                                .email("info@pandollar.com")))
                .tags(List.of(
                        new Tag().name("Lead Management")
                                .description("CRUD operations for leads and lead-to-customer conversion"),
                        new Tag().name("Customer Management")
                                .description("Read-only operations for customers created from lead conversions")
                ));
    }
}
