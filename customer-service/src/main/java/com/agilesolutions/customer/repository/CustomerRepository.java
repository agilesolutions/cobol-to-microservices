package com.agilesolutions.customer.repository;

import com.agilesolutions.customer.entity.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface CustomerRepository extends MongoRepository<Customer, String> {

    @Override
    List<Customer> findAll();

    @Query("{customerNumber:'?0'}")
    List<Customer> findCustomerByNumber(String customerNumber);

}
