package com.dasc.spring6mvc.repositories;

import com.dasc.spring6mvc.entities.Customer;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {

  List<Customer> findAllByNameIsLikeIgnoreCase(String name);

  List<Customer> findAllByEmailIsLikeIgnoreCase(String email);

  List<Customer> findAllByNameIsLikeIgnoreCaseAndEmailIsLikeIgnoreCase(String name, String email);

}
