package com.dasc.spring6mvc.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.dasc.spring6mvc.bootstrap.BootStrapData;
import com.dasc.spring6mvc.entities.Beer;
import com.dasc.spring6mvc.model.BeerStyle;
import com.dasc.spring6mvc.services.BeerCsvServiceImpl;
import jakarta.validation.ConstraintViolationException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({BootStrapData.class, BeerCsvServiceImpl.class})
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

  @Test
  void testGetBeerListByName() {
    List<Beer> beers = beerRepository.findAllByBeerNameIsLikeIgnoreCase("%IPA%");
    assertThat(beers.size()).isEqualTo(336);
  }

  @Test
  void testGetBeerListByStyle() {
    List<Beer> beers = beerRepository.findAllByBeerStyle(BeerStyle.IPA);
    assertThat(beers.size()).isEqualTo(548);
  }

  @Test
  void testGetBeerListByNameAndStyle() {
    List<Beer> beers = beerRepository.findAllByBeerNameIsLikeIgnoreCaseAndBeerStyle("%IPA%",BeerStyle.IPA);
    assertThat(beers.size()).isEqualTo(310);
  }

}