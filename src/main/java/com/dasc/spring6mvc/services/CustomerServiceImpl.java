package com.dasc.spring6mvc.services;

import com.dasc.spring6mvc.model.CustomerDTO;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService {

  private Map<UUID, CustomerDTO> customerMap;

  public CustomerServiceImpl() {
    CustomerDTO customer1 = CustomerDTO.builder()
        .id(UUID.randomUUID())
        .name("CustomerDTO 1")
        .version(1)
        .createdDate(LocalDateTime.now())
        .updateDate(LocalDateTime.now())
        .build();

    CustomerDTO customer2 = CustomerDTO.builder()
        .id(UUID.randomUUID())
        .name("CustomerDTO 2")
        .version(1)
        .createdDate(LocalDateTime.now())
        .updateDate(LocalDateTime.now())
        .build();

    CustomerDTO customer3 = CustomerDTO.builder()
        .id(UUID.randomUUID())
        .name("CustomerDTO 3")
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
  public CustomerDTO saveCustomer(CustomerDTO customer) {
    CustomerDTO savedCustomer = CustomerDTO.builder()
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
  public List<CustomerDTO> listCustomers() {
    return new ArrayList<>(customerMap.values());
  }

  @Override
  public Optional<CustomerDTO> getCustomer(UUID id) {
    return Optional.ofNullable(customerMap.get(id));
  }

  @Override
  public Optional<CustomerDTO> updateCustomer(UUID id, CustomerDTO customer) {
    var foundCustomer = customerMap.get(id);
    foundCustomer.setName(customer.getName());
    foundCustomer.setEmail(customer.getEmail());
    foundCustomer.setUpdateDate(LocalDateTime.now());
    return Optional.ofNullable(customerMap.put(id, foundCustomer));
  }

  @Override
  public Boolean deleteCustomer(UUID customerId) {
    customerMap.remove(customerId);
    return true;
  }

  @Override
  public void patchCustomer(UUID id, CustomerDTO customer) {
    var foundCustomer = customerMap.get(id);
    if (StringUtils.isNotEmpty(customer.getName())) {
      foundCustomer.setName(customer.getName());
    }
    if (StringUtils.isNotEmpty(customer.getEmail())) {
      foundCustomer.setName(customer.getEmail());
    }
    foundCustomer.setUpdateDate(LocalDateTime.now());
    customerMap.put(id, foundCustomer);
  }
}