package com.dasc.spring6mvc.repositories;

import com.dasc.spring6mvc.entities.Customer;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {

  Page<Customer> findAllByNameIsLikeIgnoreCase(String name, Pageable pageable);

  Page<Customer> findAllByEmailIsLikeIgnoreCase(String email, Pageable pageable);

  Page<Customer> findAllByNameIsLikeIgnoreCaseAndEmailIsLikeIgnoreCase(String name, String email,
      Pageable pageable);

}
