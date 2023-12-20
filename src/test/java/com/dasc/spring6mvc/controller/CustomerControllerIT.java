package com.dasc.spring6mvc.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.dasc.spring6mvc.mappers.CustomerMapper;
import com.dasc.spring6mvc.model.CustomerDTO;
import com.dasc.spring6mvc.repositories.CustomerRepository;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class CustomerControllerIT {

  @Autowired
  CustomerController customerController;
  @Autowired
  CustomerRepository customerRepository;
  @Autowired
  CustomerMapper customerMapper;

//  @Test
//  void listCustomers() {
//    var customers = customerController.listCustomers();
//
//    assertThat(customers.size()).isEqualTo(3);
//  }
//
//  @Rollback
//  @Transactional
//  @Test
//  void listCustomersEmpty() {
//    customerRepository.deleteAll();
//
//    var customers = customerController.listCustomers();
//
//    assertThat(customers.size()).isEqualTo(0);
//  }

  @Test
  void getCustomerByIdNotFound() {

    assertThrows(NotFoundException.class, () -> customerController.getCustomer(UUID.randomUUID()));
  }


  @Test
  void getCustomerById() {
    var customer = customerRepository.findAll().get(0);
    var foundCustomer = customerController.getCustomer(customer.getId());

    assertThat(foundCustomer).isNotNull();
  }

  @Rollback
  @Transactional
  @Test
  void saveNewCustomer() {
    var customerToSave = CustomerDTO.builder().name("new Customer").email("test@example.com")
        .build();

    var responseEntity = customerController.handlePost(customerToSave);

    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
    assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

    var locationUUID = responseEntity.getHeaders().getLocation().getPath().split("/");
    UUID uuid = UUID.fromString(locationUUID[4]);

    var foundCustomer = customerRepository.findById(uuid).get();
    assertThat(foundCustomer).isNotNull();

  }

  @Test
  void updateNotFoundCustomer() {
    assertThrows(NotFoundException.class, () -> {
      customerController.updateById(UUID.randomUUID(), CustomerDTO.builder().build());
    });
  }

  @Rollback
  @Transactional
  @Test
  void updateExistingCustomer() {
    var customer = customerRepository.findAll().get(0);
    var customerToUpdate = customerMapper.toCustomerDto(customer);
    customerToUpdate.setId(null);
    customerToUpdate.setVersion(null);
    customerToUpdate.setName("UPDATE");

    var responseEntity = customerController.updateById(customer.getId(), customerToUpdate);
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

    var foundCustomer = customerRepository.findById(customer.getId()).get();
    assertThat(foundCustomer.getName()).isEqualTo(customerToUpdate.getName());
  }

  @Test
  void deleteCustomerNotFound() {
    assertThrows(NotFoundException.class, () -> customerController.deleteById(UUID.randomUUID()));
  }

  @Rollback
  @Transactional
  @Test
  void deleteExistingCustomer() {
    var customer = customerRepository.findAll().get(0);
    var responseEntity = customerController.deleteById(customer.getId());
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

    assertThat(customerRepository.findById(customer.getId())).isEmpty();
  }
}