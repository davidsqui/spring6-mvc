package com.dasc.spring6mvc.services;

import com.dasc.spring6mvc.model.BeerDTO;
import com.dasc.spring6mvc.model.BeerStyle;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;

public interface BeerService {

  Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory,
      Integer pageNumber, Integer pageSize);

  Optional<BeerDTO> getBeerById(UUID id);

  BeerDTO saveNewBeer(BeerDTO beerDTO);

  void updateBeerById(UUID beerId, BeerDTO beerDTO);

  Boolean deleteBeerById(UUID beerId);

  Optional<BeerDTO> patchBeerById(UUID beerId, BeerDTO beer);
}
