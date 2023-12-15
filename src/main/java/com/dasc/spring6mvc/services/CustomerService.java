package com.dasc.spring6mvc.services;

import com.dasc.spring6mvc.entities.Customer;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {

  Customer saveCustomer(Customer customer);

  List<Customer> listCustomers();

  Optional<Customer> getCustomer(UUID customerId);

  void updateCustomer(UUID customerId, Customer customer);

  void patchCustomer(UUID customerId, Customer customer);

  void deleteCustomer(UUID customerId);

}
