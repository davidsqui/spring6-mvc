package com.dasc.spring6mvc.controller;

import static com.dasc.spring6mvc.controller.BeerControllerTest.jwtRequestPostProcessor;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dasc.spring6mvc.entities.Beer;
import com.dasc.spring6mvc.mappers.BeerMapper;
import com.dasc.spring6mvc.model.BeerDTO;
import com.dasc.spring6mvc.model.BeerStyle;
import com.dasc.spring6mvc.repositories.BeerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
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
    mockMvc = MockMvcBuilders.webAppContextSetup(wac)
        .apply(springSecurity())
        .build();
  }

  @Test
  void testPatchBeerBadName() throws Exception {
    Beer beer = beerRepository.findAll().get(0);

    Map<String, Object> beerMap = new HashMap<>();
    beerMap.put("beerName",
        "New Name 1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");

    mockMvc.perform(patch(BeerController.BEER_PATH_ID, beer.getId())
            .with(jwtRequestPostProcessor)
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
    assertThat(beer).isNotNull();
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
    Page<BeerDTO> beers = beerController.listBeers(null, null, false, 1, 2413);

    assertThat(beers.getContent().size()).isEqualTo(1000);
  }

  @Test
  void testListBeersByName() throws Exception {
    mockMvc.perform(get(BeerController.BEER_PATH)
            .with(jwtRequestPostProcessor)
            .queryParam("beerName", "IPA")
            .queryParam("pageSize", "800"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content.size()", is(336)));
  }

  @Test
  void testListBeersByStyle() throws Exception {
    mockMvc.perform(get(BeerController.BEER_PATH)
            .with(jwtRequestPostProcessor)
            .queryParam("beerStyle", BeerStyle.IPA.name())
            .queryParam("pageSize", "800"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content.size()", is(548)));
  }

  @Test
  void testListBeersByNameAndStyle() throws Exception {
    mockMvc.perform(get(BeerController.BEER_PATH)
            .with(jwtRequestPostProcessor)
            .queryParam("beerName", "IPA")
            .queryParam("beerStyle", BeerStyle.IPA.name())
            .queryParam("pageSize", "800"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content.size()", is(310)));
  }

  @Test
  void testListBeersByNameAndStylePage1() throws Exception {
    mockMvc.perform(get(BeerController.BEER_PATH)
            .with(jwtRequestPostProcessor)
            .queryParam("beerName", "IPA")
            .queryParam("beerStyle", BeerStyle.IPA.name())
            .queryParam("pageNumber", "2")
            .queryParam("pageSize", "50")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content.size()", is(50)))
        .andExpect(jsonPath("$.content[0].quantityOnHand").value(IsNull.notNullValue()));
  }

  @Test
  void testNoAuth() throws Exception {
    mockMvc.perform(get(BeerController.BEER_PATH))
        .andExpect(status().isUnauthorized());
  }

}