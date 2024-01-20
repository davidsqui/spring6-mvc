package com.dasc.spring6mvc.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.dasc.spring6mvc.bootstrap.BootStrapData;
import com.dasc.spring6mvc.entities.Customer;
import com.dasc.spring6mvc.services.BeerCsvServiceImpl;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({BootStrapData.class, BeerCsvServiceImpl.class})
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

  @Test
  void testGetBeerListByName() {
    var customers = customerRepository.findAllByNameIsLikeIgnoreCase("%1%");
    assertThat(customers.size()).isEqualTo(1);
  }

  @Test
  void testGetBeerListByEmail() {
    var customers = customerRepository.findAllByEmailIsLikeIgnoreCase("%test1%");
    assertThat(customers.size()).isEqualTo(1);
  }

  @Test
  void testGetBeerListByNameAndEmail() {
    var customers = customerRepository.findAllByNameIsLikeIgnoreCaseAndEmailIsLikeIgnoreCase("%1%",
        "%test1%");
    assertThat(customers.size()).isEqualTo(1);
  }

}