package com.dasc.spring6mvc.services;

import com.dasc.spring6mvc.model.BeerDTO;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BeerService {

  List<BeerDTO> listBeers();

  Optional<BeerDTO> getBeerById(UUID id);

  BeerDTO saveNewBeer(BeerDTO beerDTO);

  void updateBeerById(UUID beerId, BeerDTO beerDTO);

  Boolean deleteBeerById(UUID beerId);

}
