package com.dasc.spring6mvc.controller;

import static com.dasc.spring6mvc.controller.CustomerController.CUSTOMER_PATH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dasc.spring6mvc.mappers.CustomerMapper;
import com.dasc.spring6mvc.model.CustomerDTO;
import com.dasc.spring6mvc.repositories.CustomerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
class CustomerControllerIT {

  @Autowired
  CustomerController customerController;
  @Autowired
  CustomerRepository customerRepository;
  @Autowired
  CustomerMapper customerMapper;
  @Autowired
  ObjectMapper objectMapper;
  @Autowired
  WebApplicationContext wac;

  MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
  }

  @Test
  void listCustomers() {
    var customers = customerController.listCustomers(null, null);
    assertThat(customers.size()).isEqualTo(3);
  }

  @Rollback
  @Transactional
  @Test
  void listCustomersEmpty() {
    customerRepository.deleteAll();

    var customers = customerController.listCustomers(null, null);

    assertThat(customers.size()).isEqualTo(0);
  }

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

  @Test
  void listCustomersByName() throws Exception {
    mockMvc.perform(get(CUSTOMER_PATH)
            .queryParam("name", "2"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.size()", is(1)));
  }

  @Test
  void listCustomersByEmail() throws Exception {
    mockMvc.perform(get(CUSTOMER_PATH)
            .queryParam("email", "test1@example.com"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.size()", is(1)));
  }

  @Test
  void listCustomersByNameAndEmail() throws Exception {
    mockMvc.perform(get(CUSTOMER_PATH)
            .queryParam("name", "1")
            .queryParam("email", "test1@example.com"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.size()", is(1)));
  }
}