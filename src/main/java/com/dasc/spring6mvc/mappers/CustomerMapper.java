package com.dasc.spring6mvc.mappers;

import com.dasc.spring6mvc.entities.Customer;
import com.dasc.spring6mvc.model.CustomerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {

  Customer toCustomer(CustomerDTO dto);

  CustomerDTO toCustomerDto(Customer customer);
}
