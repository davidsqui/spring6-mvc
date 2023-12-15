package com.dasc.spring6mvc.repositories;

import static org.junit.jupiter.api.Assertions.*;

import com.dasc.spring6mvc.entities.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class CustomerRepositoryTest {

  @Autowired
  CustomerRepository customerRepository;

  @Test
  void saveCustomer() {
    var savedCustomer = customerRepository.save(Customer.builder().name("New Customer").build());

    assertNotNull(savedCustomer.getId());
  }

}