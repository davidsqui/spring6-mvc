package com.dasc.spring6mvc.repositories;

import com.dasc.spring6mvc.entities.Beer;
import com.dasc.spring6mvc.model.BeerStyle;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BeerRepository extends JpaRepository<Beer, UUID> {

  Page<Beer> findAllByBeerNameIsLikeIgnoreCase(String beerName, Pageable pageable);

  Page<Beer> findAllByBeerStyle(BeerStyle beerStyle, Pageable pageable);

  Page<Beer> findAllByBeerNameIsLikeIgnoreCaseAndBeerStyle(String beerName, BeerStyle beerStyle,
      Pageable pageable);

}
