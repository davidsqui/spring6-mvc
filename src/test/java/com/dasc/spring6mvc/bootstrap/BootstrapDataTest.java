package com.dasc.spring6mvc.bootstrap;

import static org.assertj.core.api.Assertions.assertThat;

import com.dasc.spring6mvc.repositories.BeerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class BootstrapDataTest {

  @Autowired
  BeerRepository beerRepository;

//  @Autowired
//  CustomerRepository customerRepository;

  BootStrapData bootstrapData;

  @BeforeEach
  void setUp() {
    bootstrapData = new BootStrapData(beerRepository);
  }

  @Test
  void Testrun() throws Exception {
    bootstrapData.run(null);

    assertThat(beerRepository.count()).isEqualTo(3);
//    assertThat(customerRepository.count()).isEqualTo(3);
  }
}
