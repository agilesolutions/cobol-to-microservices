package com.agilesolutions.customer.service;

import com.agilesolutions.customer.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    /**
     * Initializes mocks before each test execution.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    @DisplayName("Given customers, when findAllCustomer, then return all customers")
    void givenCustomers_whenFindAllCustomer_thenReturnAllCustomers() {

        // given
        when(customerRepository.findAll()).thenReturn(java.util.List.of(
                com.agilesolutions.customer.entity.Customer.builder()
                        .id("1")
                        .customerNumber("CUST001")
                        .customerName("John Doe")
                        .customerType("Individual")
                        .build(),
                com.agilesolutions.customer.entity.Customer.builder()
                        .id("2")
                        .customerNumber("CUST002")
                        .customerName("Jane Smith")
                        .customerType("Business")
                        .build()));

        // when
        reactor.core.publisher.Flux<com.agilesolutions.customer.model.CustomerResponse> customerResponseFlux = customerService.findAllCustomer();

        // then
        customerResponseFlux.collectList().blockOptional().ifPresent(customerResponses -> {
            assertEquals(2, customerResponses.size());
            assertEquals("CUST001", customerResponses.get(0).customerNumber());
            assertEquals("John Doe", customerResponses.get(0).customerName());
            assertEquals("Individual", customerResponses.get(0).customerType());
            assertEquals("CUST002", customerResponses.get(1).customerNumber());
            assertEquals("Jane Smith", customerResponses.get(1).customerName());
            assertEquals("Business", customerResponses.get(1).customerType());
        });

    }

    @Test
    @DisplayName("Given customer number, when findCustomerByNumber, then return customer")
    void givenCustomerNumber_whenFindCustomerByNumber_thenReturnCustomer() {

        // given
        when(customerRepository.findCustomerByNumber("CUST001")).thenReturn(java.util.List.of(
                com.agilesolutions.customer.entity.Customer.builder()
                        .id("1")
                        .customerNumber("CUST001")
                        .customerName("John Doe")
                        .customerType("Individual")
                        .build()));

        // when
        reactor.core.publisher.Flux<com.agilesolutions.customer.model.CustomerResponse> customerResponseFlux = customerService.findCustomerByNumber("CUST001");

        // then
        customerResponseFlux.collectList().blockOptional().ifPresent(customerResponses -> {
            assertEquals(1, customerResponses.size());
            assertEquals("CUST001", customerResponses.get(0).customerNumber());
            assertEquals("John Doe", customerResponses.get(0).customerName());
            assertEquals("Individual", customerResponses.get(0).customerType());
        });
    }
}