package com.dasc.spring6mvc.controller;

import com.dasc.spring6mvc.model.Beer;
import com.dasc.spring6mvc.service.BeerService;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@AllArgsConstructor
@RestController
public class BeerController {

  public static final String BEER_PATH = "/api/v1/beers";
  public static final String BEER_PATH_ID = BEER_PATH + "/{beerId}";

  private final BeerService beerService;

  @DeleteMapping(BEER_PATH_ID)
  public ResponseEntity deleteById(@PathVariable UUID beerId) {
    beerService.deleteBeerById(beerId);
    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }

  @PutMapping(BEER_PATH_ID)
  public ResponseEntity updateById(@PathVariable UUID beerId, @RequestBody Beer beer) {
    beerService.updateBeerById(beerId, beer);
    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }

  @PostMapping(BEER_PATH)
  public ResponseEntity handlePost(@RequestBody Beer beer) {
    Beer savedBeer = beerService.saveNewBeer(beer);

    HttpHeaders headers = new HttpHeaders();
    headers.add("Location", "/api/v1/beers/" + savedBeer.getId().toString());
    return new ResponseEntity(headers, HttpStatus.CREATED);
  }

  @GetMapping(BEER_PATH)
  public List<Beer> listBeers() {
    return beerService.listBeers();
  }

  @GetMapping(BEER_PATH_ID)
  public Beer getBeerById(@PathVariable UUID beerId) {
    log.debug("Geet Beer by beerId in controller");
    return beerService.getBeerById(beerId);
  }

}
