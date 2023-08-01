package com.dasc.spring6mvc.repositories;

import com.dasc.spring6mvc.entities.Beer;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BeerRepository extends JpaRepository<Beer, UUID> {

}
