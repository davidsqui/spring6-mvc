package com.dasc.spring6mvc.repositories;

import com.dasc.spring6mvc.entities.Beer;
import com.dasc.spring6mvc.model.BeerStyle;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BeerRepository extends JpaRepository<Beer, UUID> {

  List<Beer> findAllByBeerNameIsLikeIgnoreCase(String beerName);

  List<Beer> findAllByBeerStyle(BeerStyle beerStyle);

  List<Beer> findAllByBeerNameIsLikeIgnoreCaseAndBeerStyle(String beerName, BeerStyle beerStyle);

}
