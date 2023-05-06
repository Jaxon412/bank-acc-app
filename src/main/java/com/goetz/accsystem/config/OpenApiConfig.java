package com.goetz.accsystem.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class OpenApiConfig {

    @Bean
    public GroupedOpenApi publicApiAccount() {
        return GroupedOpenApi.builder().group("customer-api")
                .packagesToScan("com.goetz.accsystem.controller")
                .pathsToMatch("/api/customer/**")
                .build();
    }

    @Bean
    public GroupedOpenApi publicApiCustomer() {
        return GroupedOpenApi.builder().group("bank-account-api")
                .pathsToMatch("/api/bankaccount/**")
                .packagesToScan("com.goetz.accsystem.controller")
                .build();
    }

    @Bean
    public GroupedOpenApi publicApiTransaction() {
        return GroupedOpenApi.builder().group("transaction-api")
                .packagesToScan("com.goetz.accsystem.controller")
                .pathsToMatch("/api/transaction/**")
                .build();
    }
}
