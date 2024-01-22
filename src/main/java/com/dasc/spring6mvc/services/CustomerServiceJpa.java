package com.dasc.spring6mvc.services;

import com.dasc.spring6mvc.entities.Customer;
import com.dasc.spring6mvc.mappers.CustomerMapper;
import com.dasc.spring6mvc.model.CustomerDTO;
import com.dasc.spring6mvc.repositories.CustomerRepository;
import jakarta.validation.constraints.NotNull;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Primary
@RequiredArgsConstructor
public class CustomerServiceJpa implements CustomerService {

  private final CustomerRepository customerRepository;
  private final CustomerMapper customerMapper;

  private static final int DEFAULT_PAGE = 0;
  private static final int DEFAULT_PAGE_SIZE = 25;

  @Override
  public CustomerDTO saveCustomer(CustomerDTO customerDto) {
    return customerMapper.toCustomerDto(
        customerRepository.save(customerMapper.toCustomer(customerDto)));
  }

  @Override
  public Page<CustomerDTO> listCustomers(String name, String email, Integer pageNumber,
      Integer pageSize) {
    PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);
    Page<Customer> customers;
    if (StringUtils.hasText(name) && !StringUtils.hasText(email)) {
      customers = listCustomerByName(name, pageRequest);
    } else if (!StringUtils.hasText(name) && StringUtils.hasText(email)) {
      customers = listCustomerByEmail(email, pageRequest);
    } else if (StringUtils.hasText(name) && StringUtils.hasText(email)) {
      customers = listCustomersByNameAndEmail(name, email, pageRequest);
    } else {
      customers = customerRepository.findAll(pageRequest);
    }
    return customers.map(customerMapper::toCustomerDto);
  }

  public PageRequest buildPageRequest(Integer pageNumber, Integer pageSize) {
    int queryPageNumber;
    int queryPageSize;

    if (pageNumber != null && pageNumber > 0) {
      queryPageNumber = pageNumber - 1;
    } else {
      queryPageNumber = DEFAULT_PAGE;
    }

    if (pageSize == null) {
      queryPageSize = DEFAULT_PAGE_SIZE;
    } else {
      if (pageSize > 1000) {
        queryPageSize = 1000;
      } else {
        queryPageSize = pageSize;
      }
    }

    return PageRequest.of(queryPageNumber, queryPageSize);
  }

  public Page<Customer> listCustomerByName(@NotNull String name, Pageable pageable) {
    return customerRepository.findAllByNameIsLikeIgnoreCase("%" + name + "%", pageable);
  }

  public Page<Customer> listCustomerByEmail(String email, Pageable pageable) {
    return customerRepository.findAllByEmailIsLikeIgnoreCase("%" + email + "%", pageable);
  }

  public Page<Customer> listCustomersByNameAndEmail(String name, String email,
      Pageable pageable) {
    return customerRepository.findAllByNameIsLikeIgnoreCaseAndEmailIsLikeIgnoreCase(
        "%" + name + "%", "%" + email + "%", pageable);
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
