package com.agilesolutions.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRoutes(RouteLocatorBuilder builder) {
        return builder.routes()

                // Requests to /legacy/accounts/{id} will now go to our mock service. This allows us to test the gateway routing without needing the actual z/OS Connect service running.
                .route("mock_legacy_accounts", r -> r.path("/legacy/accounts/**")
                        .uri("http://localhost:8090/zos/accounts")) // Mock z/OS Connect

                // Requests to /legacy/customer/{id} will now go to our mock service. This allows us to test the gateway routing without needing the actual z/OS Connect service running.
                .route("mock_legacy_customer", r -> r.path("/legacy/customer/**")
                        .uri("http://localhost:8090/zos/customers")) // Mock z/OS Connect

                // Route to legacy COBOL via z/OS Connect
                .route("legacy_accounts", r -> r.path("/legacy/accounts/**")
                        .uri("http://zosconnect-host:9080/zos/accounts")) // z/OS Connect endpoint
                .route("legacy_customer", r -> r.path("/legacy/customers/**")
                        .uri("http://zosconnect-host:9080/zos/customers"))

                // Route to new microservices
                .route("new_account_service", r -> r.path("/api/accounts/**")
                        .uri("http://localhost:8082"))
                .route("new_customer_service", r -> r.path("/api/customers/**")
                        .uri("http://localhost:8081"))
                .build();
    }
}
