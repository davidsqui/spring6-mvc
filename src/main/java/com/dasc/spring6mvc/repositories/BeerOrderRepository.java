package com.dasc.spring6mvc.repositories;

import com.dasc.spring6mvc.entities.BeerOrder;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BeerOrderRepository extends JpaRepository<BeerOrder, UUID> {

}
