package com.dasc.spring6mvc.repositories;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.dasc.spring6mvc.entities.Customer;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class CustomerRepositoryTest {

  @Autowired
  CustomerRepository customerRepository;

  @Test
  void saveCustomerWithInvalidNameSize() {
    assertThrows(ConstraintViolationException.class, () -> {
      customerRepository.save(Customer.builder()
          .name("New Customervsdvvvvvvvvvv8084488888888888888888888888888888888885505 5sd6gsbdbd0")
          .email("test@gmail.com")
          .build());
      customerRepository.flush();
    });
  }

  @Test
  void saveCustomer() {
    var savedCustomer = customerRepository.save(Customer.builder()
        .name("New Customer")
        .email("test@gmail.com")
        .build());

    customerRepository.flush();

    assertNotNull(savedCustomer.getId());
  }

}