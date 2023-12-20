package com.dasc.spring6mvc.controller;

import com.dasc.spring6mvc.model.CustomerDTO;
import com.dasc.spring6mvc.services.CustomerService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class CustomerController {

  public static final String CUSTOMER_PATH = "/api/v1/customers";
  public static final String CUSTOMER_PATH_ID = CUSTOMER_PATH + "/{customerId}";

  private final CustomerService customerService;

  @PostMapping(CUSTOMER_PATH)
  public ResponseEntity handlePost(CustomerDTO customer) {
    var savedCustomer = customerService.saveCustomer(customer);
    HttpHeaders headers = new HttpHeaders();
    headers.add("Location", "/api/v1/customers/" + savedCustomer.getId().toString());
    return new ResponseEntity(headers, HttpStatus.CREATED);
  }

  @GetMapping(CUSTOMER_PATH)
  public ResponseEntity listCustomers() {
    var customers = customerService.listCustomers();
    return new ResponseEntity(customers, HttpStatus.OK);
  }

  @GetMapping(CUSTOMER_PATH_ID)
  public ResponseEntity getCustomer(@PathVariable UUID customerId) {
    var customer = customerService.getCustomer(customerId).orElseThrow(NotFoundException::new);
    return new ResponseEntity(customer, HttpStatus.OK);
  }

  @PutMapping(CUSTOMER_PATH_ID)
  public ResponseEntity updateById(@PathVariable UUID customerId,
      @RequestBody CustomerDTO customer) {
    customerService.updateCustomer(customerId, customer)
        .orElseThrow(NotFoundException::new);
    return new ResponseEntity(HttpStatus.NO_CONTENT);

  }

  @DeleteMapping(CUSTOMER_PATH_ID)
  public ResponseEntity deleteById(@PathVariable UUID customerId) {
    if (!customerService.deleteCustomer(customerId)) {
      throw new NotFoundException();
    }
    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }

  @PatchMapping(CUSTOMER_PATH_ID)
  public ResponseEntity patchCustomer(@PathVariable UUID customerId,
      @RequestBody CustomerDTO customer) {
    customerService.patchCustomer(customerId, customer);
    return new ResponseEntity(HttpStatus.NO_CONTENT);

  }

}
