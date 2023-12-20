package com.dasc.spring6mvc.controller;

import static com.dasc.spring6mvc.controller.CustomerController.CUSTOMER_PATH;
import static com.dasc.spring6mvc.controller.CustomerController.CUSTOMER_PATH_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dasc.spring6mvc.model.CustomerDTO;
import com.dasc.spring6mvc.services.CustomerService;
import com.dasc.spring6mvc.services.CustomerServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

  @Autowired
  MockMvc mockMvc;
  @Autowired
  ObjectMapper mapper;

  @MockBean
  CustomerService customerService;

  CustomerServiceImpl customerServiceImpl = new CustomerServiceImpl();

  @Captor
  ArgumentCaptor<UUID> uuidArgumentCaptor;
  @Captor
  ArgumentCaptor<CustomerDTO> customerArgumentCaptor;

  @Test
  void getCustomers() throws Exception {

    given(customerService.listCustomers()).willReturn(customerServiceImpl.listCustomers());

    mockMvc.perform(get(CUSTOMER_PATH)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.length()", is(3)));
  }

  @Test
  void getCustomerById() throws Exception {

    var returnedCustomer = customerServiceImpl.listCustomers().get(0);
    given(customerService.getCustomer(returnedCustomer.getId())).willReturn(
        Optional.of(returnedCustomer));

    mockMvc.perform(get(CUSTOMER_PATH_ID, returnedCustomer.getId())
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id", is(returnedCustomer.getId().toString())))
        .andExpect(jsonPath("$.name", is(returnedCustomer.getName())));

  }

  @Test
  void getCustomerByIdNotFound() throws Exception {
    given(customerService.getCustomer(any(UUID.class))).willReturn(Optional.empty());
    mockMvc.perform(get(CUSTOMER_PATH_ID, UUID.randomUUID()))
        .andExpect(status().isNotFound());
  }

  @Test
  void createCustomer() throws Exception {
    var customerToSave = CustomerDTO.builder().name("new customer").email("customer@gmail.com")
        .build();
    var returnedCustomer = CustomerDTO.builder().id(UUID.randomUUID()).name("new customer")
        .email("customer@gmail.com").build();

    given(customerService.saveCustomer(any(CustomerDTO.class))).willReturn(returnedCustomer);

    mockMvc.perform(post(CUSTOMER_PATH)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(customerToSave)))
        .andExpect(status().isCreated())
        .andExpect(header().exists("location"))
        .andExpect(header().string("location",
            "/api/v1/customers/" + returnedCustomer.getId().toString()));
  }

  @Test
  void updateCustomer() throws Exception {
    var customerToUpdate = customerServiceImpl.listCustomers().get(0);

    given(customerService.updateCustomer(any(UUID.class), any(CustomerDTO.class))).willReturn(
        Optional.of(customerToUpdate));

    mockMvc.perform(put(CUSTOMER_PATH_ID, customerToUpdate.getId())
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(customerToUpdate)))
        .andExpect(status().isNoContent());

    verify(customerService).updateCustomer(any(UUID.class), any(CustomerDTO.class));

  }

  @Test
  void deleteCustomer() throws Exception {
    var customerToDelete = customerServiceImpl.listCustomers().get(0);

    given(customerService.deleteCustomer(any(UUID.class))).willReturn(true);

    mockMvc.perform(delete(CUSTOMER_PATH_ID, customerToDelete.getId())
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());

    verify(customerService).deleteCustomer(uuidArgumentCaptor.capture());

    assertThat(customerToDelete.getId()).isEqualTo(uuidArgumentCaptor.getValue());
  }

  @Test
  void patchCustomer() throws Exception {
    var customerToPatch = customerServiceImpl.listCustomers().get(0);

    Map<String, Object> beerMap = new HashMap<>();
    beerMap.put("name", "new beer name");

    mockMvc.perform(patch(CUSTOMER_PATH_ID, customerToPatch.getId())
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(beerMap)))
        .andExpect(status().isNoContent());

    verify(customerService).patchCustomer(uuidArgumentCaptor.capture(),
        customerArgumentCaptor.capture());

    assertThat(customerToPatch.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    assertThat(beerMap.get("name")).isEqualTo(customerArgumentCaptor.getValue().getName());
    assertThat(beerMap.get("email")).isNull();

  }

}