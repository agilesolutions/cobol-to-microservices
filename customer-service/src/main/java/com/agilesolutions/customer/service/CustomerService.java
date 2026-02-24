package com.agilesolutions.customer.service;

import com.agilesolutions.customer.entity.Customer;
import com.agilesolutions.customer.model.CustomerResponse;
import com.agilesolutions.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public Flux<CustomerResponse> findAllCustomer(List<Long> clientIds) {
        return Flux.fromStream(customerRepository.findAll().stream()
                .map(this::mapToDomain));
    }

    public Flux<CustomerResponse> findCustomerByNumber(String number) {
        return Flux.fromStream(customerRepository.findCustomerByNumber(number).stream()
                .map(this::mapToDomain));
    }

    private CustomerResponse mapToDomain(Customer entity) {
        return CustomerResponse.builder()
                .customerNumber(entity.getCustomerNumber())
                .customerType(entity.getCustomerType())
                .customerName(entity.getCustomerName())
                .build();
    }

}
