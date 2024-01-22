package com.dasc.spring6mvc.services;

import com.dasc.spring6mvc.model.CustomerDTO;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;

public interface CustomerService {

  CustomerDTO saveCustomer(CustomerDTO customer);

  Page<CustomerDTO> listCustomers(String name, String email, Integer pageNumber, Integer pageSize);

  Optional<CustomerDTO> getCustomer(UUID customerId);

  Optional<CustomerDTO> updateCustomer(UUID customerId, CustomerDTO customer);

  void patchCustomer(UUID customerId, CustomerDTO customer);

  Boolean deleteCustomer(UUID customerId);

}
