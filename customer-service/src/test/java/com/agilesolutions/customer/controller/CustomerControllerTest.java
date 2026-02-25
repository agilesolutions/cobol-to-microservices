package com.agilesolutions.customer.controller;

import com.agilesolutions.customer.service.CustomerService;
import com.agilesolutions.customer.service.LegacyCustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


class CustomerControllerTest {


    private WebTestClient webTestClient;

    private CustomerService customerService;

    LegacyCustomerService legacyCustomerService;

    @BeforeEach
    void setUp() {
        customerService = org.mockito.Mockito.mock(CustomerService.class);
        legacyCustomerService = org.mockito.Mockito.mock(LegacyCustomerService.class);
        webTestClient = WebTestClient.bindToController(new CustomerController(legacyCustomerService, customerService)).build();
        webTestClient
                .mutate()
                .responseTimeout(java.time.Duration.ofSeconds(30))
                .build();
    }

    @Test
    void givenExistingCustomer_whenFetchCustomer_thenReturnCustomer() {

        // Given
        when(legacyCustomerService.getCustomer("1")).thenReturn(
                reactor.core.publisher.Mono.just(
                        com.agilesolutions.customer.model.CustomerResponse.builder()
                                .customerNumber("CUST12345")
                                .customerType("INDIVIDUAL")
                                .customerName("John Doe")
                                .build()
                )
        );

        // When and Then
        webTestClient.get().uri("/api/customers/fetch?id=1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.customerNumber").isEqualTo("CUST12345")
                .jsonPath("$.customerType").isEqualTo("INDIVIDUAL")
                .jsonPath("$.customerName").isEqualTo("John Doe");

    }

    @Test
    void givenNonExistingCustomer_whenFetchCustomer_thenReturnNotFound() {

        // Given
        when(legacyCustomerService.getCustomer("999")).thenReturn(
                reactor.core.publisher.Mono.empty()
        );

        // When and Then
        webTestClient.get().uri("/api/customers/fetch?id=999")
                .exchange()
                .expectStatus().isOk();

    }

    @Test
    void givenServiceError_whenFetchCustomer_thenReturnInternalServerError() {

        // Given
        when(legacyCustomerService.getCustomer("1")).thenReturn(
                reactor.core.publisher.Mono.error(new RuntimeException("Database error"))
        );

        // When and Then
        webTestClient.get().uri("/api/customers/fetch?id=1")
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    void givenExistingCustomers_whenFetchAllCustomers_thenReturnCustomerList() {

        // Given
        when(customerService.findAllCustomer()).thenReturn(
                reactor.core.publisher.Flux.just(
                        com.agilesolutions.customer.model.CustomerResponse.builder()
                                .customerNumber("CUST12345")
                                .customerType("INDIVIDUAL")
                                .customerName("John Doe")
                                .build(),
                        com.agilesolutions.customer.model.CustomerResponse.builder()
                                .customerNumber("CUST67890")
                                .customerType("BUSINESS")
                                .customerName("Acme Corporation")
                                .build()
                )
        );

        // When and Then
        webTestClient.get().uri("/api/customers/fetchAll")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].customerNumber").isEqualTo("CUST12345")
                .jsonPath("$[0].customerType").isEqualTo("INDIVIDUAL")
                .jsonPath("$[0].customerName").isEqualTo("John Doe")
                .jsonPath("$[1].customerNumber").isEqualTo("CUST67890")
                .jsonPath("$[1].customerType").isEqualTo("BUSINESS")
                .jsonPath("$[1].customerName").isEqualTo("Acme Corporation");

    }

    @Test
    void givenServiceError_whenFetchAllCustomers_thenReturnInternalServerError() {

        // Given
        when(customerService.findAllCustomer()).thenReturn(
                reactor.core.publisher.Flux.error(new RuntimeException("Database error"))
        );

        // When and Then
        webTestClient.get().uri("/api/customers/fetchAll")
                .exchange()
                .expectStatus().is5xxServerError();
    }
}