package com.dasc.spring6mvc.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dasc.spring6mvc.entities.Beer;
import com.dasc.spring6mvc.mappers.BeerMapper;
import com.dasc.spring6mvc.model.BeerDTO;
import com.dasc.spring6mvc.repositories.BeerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
class BeerControllerIT {

  @Autowired
  BeerController beerController;
  @Autowired
  BeerRepository beerRepository;

  @Autowired
  BeerMapper beerMapper;

  @Autowired
  ObjectMapper objectMapper;

  @Autowired
  WebApplicationContext wac;

  MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
  }

  @Test
  void testPatchBeerBadName() throws Exception {
    Beer beer = beerRepository.findAll().get(0);

    Map<String, Object> beerMap = new HashMap<>();
    beerMap.put("beerName",
        "New Name 1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");

    mockMvc.perform(patch(BeerController.BEER_PATH_ID, beer.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(beerMap)))
        .andExpect(status().isBadRequest());

  }

  @Test
  void deleteByIdNotFound() {
    assertThrows(NotFoundException.class, () -> {
      beerController.deleteById(UUID.randomUUID());
    });
  }

  @Rollback
  @Transactional
  @Test
  void deleteByIdFound() {
    Beer beer = beerRepository.findAll().get(0);

    ResponseEntity responseEntity = beerController.deleteById(beer.getId());

    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
    assertThat(beerRepository.findById(beer.getId())).isEmpty();
  }

  @Rollback
  @Transactional
  @Test
  void updateExistingBeer() {
    final String UPDATED = "UPDATED";
    Beer beer = beerRepository.findAll().get(0);
    BeerDTO beerDTO = beerMapper.beerToBeerDto(beer);
    beerDTO.setId(null);
    beerDTO.setVersion(null);
    beerDTO.setBeerName(UPDATED);

    ResponseEntity responseEntity = beerController.updateById(beer.getId(), beerDTO);
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

    Beer updatedBeer = beerRepository.findById(beer.getId()).get();
    assertThat(updatedBeer.getBeerName()).isEqualTo(UPDATED);
  }

  @Rollback
  @Transactional
  @Test
  void saveNewBeerTest() {
    BeerDTO beerDTO = BeerDTO.builder()
        .beerName("New beer")
        .build();

    ResponseEntity responseEntity = beerController.handlePost(beerDTO);

    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
    assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

    String[] locationUUID = responseEntity.getHeaders().getLocation().getPath().split("/");
    UUID savedUUID = UUID.fromString(locationUUID[4]);

    Beer beer = beerRepository.findById(savedUUID).get();
    assertThat(savedUUID).isNotNull();
  }

  @Test
  void testGetByIdNotFound() {
    assertThrows(NotFoundException.class, () -> {
      beerController.getBeerById(UUID.randomUUID());
    });
  }

  @Test
  void testGetById() {
    Beer beer = beerRepository.findAll().get(0);
    BeerDTO dto = beerController.getBeerById(beer.getId());

    assertThat(dto).isNotNull();
  }

  @Test
  void testListBeers() {
    List<BeerDTO> beers = beerController.listBeers();

    assertThat(beers.size()).isEqualTo(3);
  }

}