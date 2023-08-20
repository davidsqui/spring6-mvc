package com.dasc.spring6mvc.bootstrap;

import static org.assertj.core.api.Assertions.assertThat;

import com.dasc.spring6mvc.repositories.BeerRepository;
import com.dasc.spring6mvc.repositories.CustomerRepository;
import com.dasc.spring6mvc.services.BeerCsvService;
import com.dasc.spring6mvc.services.BeerCsvServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(BeerCsvServiceImpl.class)
class BootstrapDataTest {

  @Autowired
  BeerRepository beerRepository;

  @Autowired
  CustomerRepository customerRepository;

  @Autowired
  BeerCsvService beerCsvService;

  BootStrapData bootstrapData;

  @BeforeEach
  void setUp() {
    bootstrapData = new BootStrapData(beerRepository, customerRepository, beerCsvService);
  }

  @Test
  void Testrun() throws Exception {
    bootstrapData.run(null);

    assertThat(beerRepository.count()).isEqualTo(2413);
    assertThat(customerRepository.count()).isEqualTo(3);
  }
}
