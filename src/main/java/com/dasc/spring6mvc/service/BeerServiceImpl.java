package com.dasc.spring6mvc.service;

import com.dasc.spring6mvc.model.Beer;
import com.dasc.spring6mvc.model.BeerStyle;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BeerServiceImpl implements BeerService {

  @Override
  public Beer getBeerById(UUID id) {
    log.debug("Get Beer ID was called");
    return Beer.builder()
        .id(id)
        .version(1)
        .beerName("Galaxy Cat")
        .beerStyle(BeerStyle.ALE)
        .upc("123456")
        .price(new BigDecimal("12.99"))
        .quantityOnHand(122)
        .createdDate(LocalDateTime.now())
        .updateDate(LocalDateTime.now())
        .build();
  }
}
