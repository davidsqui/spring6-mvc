package com.dasc.spring6mvc.services;

import com.dasc.spring6mvc.entities.Customer;
import com.dasc.spring6mvc.mappers.CustomerMapper;
import com.dasc.spring6mvc.model.CustomerDTO;
import com.dasc.spring6mvc.repositories.CustomerRepository;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Primary
@RequiredArgsConstructor
public class CustomerServiceJpa implements CustomerService {

  private final CustomerRepository customerRepository;
  private final CustomerMapper customerMapper;

  @Override
  public CustomerDTO saveCustomer(CustomerDTO customerDto) {
    return customerMapper.toCustomerDto(
        customerRepository.save(customerMapper.toCustomer(customerDto)));
  }

  @Override
  public List<CustomerDTO> listCustomers(String name, String email) {
    List<Customer> customers;
    if (StringUtils.hasText(name) && !StringUtils.hasText(email)) {
      customers = listCustomerByName(name);
    } else if (!StringUtils.hasText(name) && StringUtils.hasText(email)) {
      customers = listCustomerByEmail(email);
    } else if (StringUtils.hasText(name) && StringUtils.hasText(email)) {
      customers = listCustomersByNameAndEmail(name, email);
    } else {
      customers = customerRepository.findAll();
    }
    return customers.stream()
        .map(customerMapper::toCustomerDto)
        .collect(Collectors.toList());
  }

  public List<Customer> listCustomerByName(@NotNull String name) {
    return customerRepository.findAllByNameIsLikeIgnoreCase("%" + name + "%");
  }

  public List<Customer> listCustomerByEmail(String email) {
    return customerRepository.findAllByEmailIsLikeIgnoreCase("%" + email + "%");
  }

  public List<Customer> listCustomersByNameAndEmail(String name, String email) {
    return customerRepository.findAllByNameIsLikeIgnoreCaseAndEmailIsLikeIgnoreCase(
        "%" + name + "%", "%" + email + "%");
  }

  @Override
  public Optional<CustomerDTO> getCustomer(UUID customerId) {
    return customerRepository.findById(customerId)
        .map(customerMapper::toCustomerDto);
  }

  @Override
  public Optional<CustomerDTO> updateCustomer(UUID customerId, CustomerDTO customer) {
    AtomicReference<Optional<CustomerDTO>> customerAtomicReference = new AtomicReference<>();
    customerAtomicReference.set(Optional.empty());
    customerRepository.findById(customerId)
        .ifPresent(foundCustomer -> {
          foundCustomer.setName(customer.getName());
          foundCustomer.setEmail(customer.getEmail());
          var savedCustomer = customerRepository.save(foundCustomer);
          customerAtomicReference.set(Optional.of(customerMapper.toCustomerDto(savedCustomer)));
        });
    return customerAtomicReference.get();
  }

  @Override
  public void patchCustomer(UUID customerId, CustomerDTO customer) {

  }

  @Override
  public Boolean deleteCustomer(UUID customerId) {
    if (customerRepository.existsById(customerId)) {
      customerRepository.deleteById(customerId);
      return true;
    }
    return false;
  }
}
