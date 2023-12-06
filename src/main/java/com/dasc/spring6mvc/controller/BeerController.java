package com.dasc.spring6mvc.controller;

import com.dasc.spring6mvc.model.BeerDTO;
import com.dasc.spring6mvc.model.BeerStyle;
import com.dasc.spring6mvc.services.BeerService;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class BeerController {

  public static final String BEER_PATH = "/api/v1/beers";
  public static final String BEER_PATH_ID = BEER_PATH + "/{beerId}";

  private final BeerService beerService;

  @PatchMapping(BEER_PATH_ID)
  public ResponseEntity updateBeerPatchById(@PathVariable("beerId") UUID beerId,
      @RequestBody BeerDTO beer) {

    beerService.patchBeerById(beerId, beer);

    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }

  @DeleteMapping(BEER_PATH_ID)
  public ResponseEntity deleteById(@PathVariable UUID beerId) {
    if (!beerService.deleteBeerById(beerId)) {
      throw new NotFoundException();
    }
    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }

  @PutMapping(BEER_PATH_ID)
  public ResponseEntity updateById(@PathVariable UUID beerId, @RequestBody BeerDTO beerDTO) {
    beerService.updateBeerById(beerId, beerDTO);
    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }

  @PostMapping(BEER_PATH)
  public ResponseEntity handlePost(@Validated @RequestBody BeerDTO beerDTO) {
    BeerDTO savedBeerDTO = beerService.saveNewBeer(beerDTO);

    HttpHeaders headers = new HttpHeaders();
    headers.add("Location", "/api/v1/beers/" + savedBeerDTO.getId().toString());
    return new ResponseEntity(headers, HttpStatus.CREATED);
  }

  @GetMapping(BEER_PATH)
  public Page<BeerDTO> listBeers(
      @RequestParam(required = false) String beerName,
      @RequestParam(required = false) BeerStyle beerStyle,
      @RequestParam(required = false) Boolean showInventory,
      @RequestParam(required = false) Integer pageNumber,
      @RequestParam(required = false) Integer pageSize) {
    return beerService.listBeers(beerName, beerStyle, showInventory, pageNumber, pageSize);
  }

  @GetMapping(BEER_PATH_ID)
  public BeerDTO getBeerById(@PathVariable UUID beerId) {
    log.debug("Geet Beer by beerId in controller");
    return beerService.getBeerById(beerId).orElseThrow(NotFoundException::new);
  }

}
