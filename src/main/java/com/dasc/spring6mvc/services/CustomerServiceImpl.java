package com.dasc.spring6mvc.services;

import com.dasc.spring6mvc.entities.Customer;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService {

  private Map<UUID, Customer> customerMap;

  public CustomerServiceImpl() {
    Customer customer1 = Customer.builder()
        .id(UUID.randomUUID())
        .name("Customer 1")
        .version(1)
        .createdDate(LocalDateTime.now())
        .updateDate(LocalDateTime.now())
        .build();

    Customer customer2 = Customer.builder()
        .id(UUID.randomUUID())
        .name("Customer 2")
        .version(1)
        .createdDate(LocalDateTime.now())
        .updateDate(LocalDateTime.now())
        .build();

    Customer customer3 = Customer.builder()
        .id(UUID.randomUUID())
        .name("Customer 3")
        .version(1)
        .createdDate(LocalDateTime.now())
        .updateDate(LocalDateTime.now())
        .build();

    customerMap = new HashMap<>();
    customerMap.put(customer1.getId(), customer1);
    customerMap.put(customer2.getId(), customer2);
    customerMap.put(customer3.getId(), customer3);

  }

  @Override
  public Customer saveCustomer(Customer customer) {
    Customer savedCustomer = Customer.builder()
        .id(UUID.randomUUID())
        .version(1)
        .updateDate(LocalDateTime.now())
        .createdDate(LocalDateTime.now())
        .name(customer.getName())
        .build();

    customerMap.put(savedCustomer.getId(), savedCustomer);
    return savedCustomer;
  }

  @Override
  public List<Customer> listCustomers() {
    return new ArrayList<>(customerMap.values());
  }

  @Override
  public void updateById(UUID id, Customer customer) {
    var foundCustomer = customerMap.get(id);
    foundCustomer.setName(customer.getName());
    foundCustomer.setUpdateDate(LocalDateTime.now());
    customerMap.put(id, foundCustomer);
  }

  @Override
  public void deleteById(UUID customerId) {
    customerMap.remove(customerId);
  }
}