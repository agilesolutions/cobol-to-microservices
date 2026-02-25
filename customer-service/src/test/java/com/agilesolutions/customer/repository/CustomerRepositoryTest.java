package com.agilesolutions.customer.repository;

import com.agilesolutions.customer.entity.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@EnableMongoRepositories(basePackages = "com.agilesolutions.customer.repository")
class CustomerRepositoryTest extends BaseMongoDBIntegrationTest {

    @Autowired
    private CustomerRepository repository;

    @BeforeEach
    void setUp() {

        repository.saveAll(List.of(
                Customer.builder().id("1").customerNumber("CUST001").customerName("John Doe").customerType("Individual").build(),
                Customer.builder().id("2").customerNumber("CUST002").customerName("Jane Smith").customerType("Business").build(),
                Customer.builder().id("3").customerNumber("CUST003").customerName("Alice Johnson").customerType("Individual").build(),
                Customer.builder().id("4").customerNumber("CUST004").customerName("Bob Brown").customerType("Business").build(),
                Customer.builder().id("5").customerNumber("CUST005").customerName("Charlie Davis").customerType("Individual").build()
        ));
    }

    @Test
    void givenExistingCustomer_whenFetchCustomer_thenReturnCustomer() {

        List<Customer> customers = repository.findCustomerByNumber("CUST002");
        assertNotNull(customers);
        assertEquals(1, customers.size());
        assertEquals("CUST002", customers.get(0).getCustomerNumber());
        assertEquals("Jane Smith", customers.get(0).getCustomerName());
        assertEquals("Business", customers.get(0).getCustomerType());
    }

    @Test
    void givenNewCustomer_whenSaveNewCustomer_thenPersistCustomer() {
        Customer newCustomer = Customer.builder()
                .customerNumber("CUST006")
                .customerName("David Wilson")
                .customerType("Individual")
                .build();

        Customer savedCustomer = repository.save(newCustomer);

        assertNotNull(savedCustomer);
        assertNotNull(savedCustomer.getId());
        assertEquals("CUST006", savedCustomer.getCustomerNumber());
        assertEquals("David Wilson", savedCustomer.getCustomerName());
        assertEquals("Individual", savedCustomer.getCustomerType());
    }

    @Test
    void givenExistingCustomers_whenFindAll_thenReturnAllCustomers() {
        List<Customer> customers = repository.findAll();
        assertNotNull(customers);
        assertEquals(5, customers.size());
    }
}