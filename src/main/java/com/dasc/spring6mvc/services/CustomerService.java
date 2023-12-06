package com.dasc.spring6mvc.services;

import com.dasc.spring6mvc.entities.Customer;
import java.util.List;
import java.util.UUID;

public interface CustomerService {

  Customer saveCustomer(Customer customer);

  List<Customer> listCustomers();

  void updateById(UUID id, Customer customer);

  void deleteById(UUID customerId);
}
