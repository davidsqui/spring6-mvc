package com.dasc.spring6mvc.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.dasc.spring6mvc.entities.Beer;
import com.dasc.spring6mvc.model.BeerStyle;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class BeerRepositoryTest {

  @Autowired
  BeerRepository beerRepository;

  @Test
  void testSaveBeerNameTooLong() {
    assertThrows(ConstraintViolationException.class, () -> {
      beerRepository.save(Beer.builder()
          .beerName(
              "My beer asfsdfsssssssssssssssffffffffdsfsdfsfsdvdfsbdgfbdfbdfbdbfgbfbdbdbdfbfsbdgfbdfbdfbdbfgbfbdbdbdfbfsbdgfbdfbdfbdbfgbfbdbdbdfbfsbdgfbdfbdfbdbfgbfbdbdbdfbfsbdgfbdfbdfbdbfgbfbdbdbdfbfsbdgfbdfbdfbdbfgbfbdbdbdfbfsbdgfbdfbdfbdbfgbfbdbdbdfbfsbdgfbdfbdfbdbfgbfbdbdbdfbMy beer asfsdfsssssssssssssssffffffffdsfsdfsfsdvdfsbdgfbdfbdfbdbfgbfbdbdbdfbfsbdgfbdfbdfbdbfgbfbdbdbdfbfsbdgfbdfbdfbdbfgbfbdbdbdfbfsbdgfbdfbdfbdbfgbfbdbdbdfbfsbdgfbdfbdfbdbfgbfbdbdbdfbfsbdgfbdfbdfbdbfgbfbdbdbdfbfsbdgfbdfbdfbdbfgbfbdbdbdfbfsbdgfbdfbdfbdbfgbfbdbdbdfb")
          .build());
      beerRepository.flush();
    });
  }

  @Test
  void testSaveBeer() {
    Beer savedBeer = beerRepository.save(Beer.builder()
        .beerName("My beer")
        .beerStyle(BeerStyle.GOSE)
        .upc("new upc")
        .build());

    beerRepository.flush();

    assertThat(savedBeer).isNotNull();
    assertThat(savedBeer.getId()).isNotNull();
  }

}