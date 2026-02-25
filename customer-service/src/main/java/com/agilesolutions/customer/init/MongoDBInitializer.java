package com.agilesolutions.customer.init;

import com.agilesolutions.customer.entity.Customer;
import com.agilesolutions.customer.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class MongoDBInitializer implements CommandLineRunner {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public void run(String... args) {


        customerRepository.saveAll(List.of(
                Customer.builder().id("1").customerNumber("CUST001").customerName("John Doe").customerType("Individual").build(),
                Customer.builder().id("2").customerNumber("CUST002").customerName("Jane Smith").customerType("Business").build(),
                Customer.builder().id("3").customerNumber("CUST003").customerName("Alice Johnson").customerType("Individual").build(),
                Customer.builder().id("4").customerNumber("CUST004").customerName("Bob Brown").customerType("Business").build(),
                Customer.builder().id("5").customerNumber("CUST005").customerName("Charlie Davis").customerType("Individual").build()
        ));

        log.info("MongoDB records saved successfully-------");

    }
}
