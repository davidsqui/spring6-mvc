package com.dasc.spring6mvc.services;

import static org.assertj.core.api.Assertions.assertThat;

import com.dasc.spring6mvc.model.BeerCSVRecord;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

class BeerCsvServiceTest {

  BeerCsvService beerCsvService = new BeerCsvServiceImpl();

  @Test
  void convertCSV() throws FileNotFoundException {

    File file = ResourceUtils.getFile("classpath:csvdata/beers.csv");

    List<BeerCSVRecord> recs = beerCsvService.convertCSV(file);

    System.out.println(recs.size());

    assertThat(recs.size()).isGreaterThan(0);
  }
}