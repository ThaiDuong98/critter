package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Customer;

import java.util.List;

public interface CustomerService {
    public Customer saveCustomer(Customer customer);
    public Customer getCustomerById(Long customerId);
    public List<Customer> getAllCustomers();
    Customer getOwnByPetId(Long id);
}
