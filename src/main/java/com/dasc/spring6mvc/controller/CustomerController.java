package com.dasc.spring6mvc.controller;

import com.dasc.spring6mvc.entities.Customer;
import com.dasc.spring6mvc.services.CustomerService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
@RestController
public class CustomerController {

  private final CustomerService customerService;

  @PostMapping
  public ResponseEntity handlePost(Customer customer) {
    var customerSaved = customerService.saveCustomer(customer);
    HttpHeaders headers = new HttpHeaders();
    headers.add("Location", "/api/v1/customers" + customerSaved.getId().toString());
    return new ResponseEntity(headers, HttpStatus.CREATED);
  }

  @GetMapping
  public ResponseEntity listCustomers() {
    var customers = customerService.listCustomers();
    return new ResponseEntity(customers, HttpStatus.OK);
  }

  @PutMapping("{customerId}")
  public ResponseEntity updateById(@PathVariable UUID customerId, @RequestBody Customer customer) {
    customerService.updateById(customerId, customer);
    return new ResponseEntity(HttpStatus.NO_CONTENT);

  }

  @DeleteMapping("{customerId}")
  public ResponseEntity deleteById(@PathVariable UUID customerId) {
    customerService.deleteById(customerId);
    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }

}
