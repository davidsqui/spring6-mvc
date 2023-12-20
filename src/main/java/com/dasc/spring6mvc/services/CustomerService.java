package com.dasc.spring6mvc.services;

import com.dasc.spring6mvc.model.CustomerDTO;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {

  CustomerDTO saveCustomer(CustomerDTO customer);

  List<CustomerDTO> listCustomers();

  Optional<CustomerDTO> getCustomer(UUID customerId);

  Optional<CustomerDTO> updateCustomer(UUID customerId, CustomerDTO customer);

  void patchCustomer(UUID customerId, CustomerDTO customer);

  Boolean deleteCustomer(UUID customerId);

}
