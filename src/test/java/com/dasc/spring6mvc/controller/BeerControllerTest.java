package com.dasc.spring6mvc.controller;

import static com.dasc.spring6mvc.controller.BeerController.BEER_PATH;
import static com.dasc.spring6mvc.controller.BeerController.BEER_PATH_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dasc.spring6mvc.model.BeerDTO;
import com.dasc.spring6mvc.services.BeerService;
import com.dasc.spring6mvc.services.BeerServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@WebMvcTest(BeerController.class)
class BeerControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  BeerService beerService;

  BeerServiceImpl beerServiceImpl;

  @BeforeEach
  void setUp() {
    beerServiceImpl = new BeerServiceImpl();
  }

  @Test
  void testDeleteBeer() throws Exception {
    BeerDTO beerDTO = beerServiceImpl.listBeers(null, null, false).get(0);

    given(beerService.deleteBeerById(beerDTO.getId())).willReturn(true);

    mockMvc.perform(delete(BEER_PATH_ID, beerDTO.getId())
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());

    ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);
    verify(beerService).deleteBeerById(uuidArgumentCaptor.capture());

    assertThat(beerDTO.getId()).isEqualTo(uuidArgumentCaptor.getValue());
  }


  @Test
  void testUpdateBeer() throws Exception {
    BeerDTO beerDTO = beerServiceImpl.listBeers(null, null, false).get(0);
    mockMvc.perform(put(BEER_PATH_ID, beerDTO.getId())
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(beerDTO)))
        .andExpect(status().isNoContent());

    verify(beerService).updateBeerById(any(UUID.class), any(BeerDTO.class));

  }

  @Test
  void testCreateNewBeer() throws Exception {
    BeerDTO beerDTO = beerServiceImpl.listBeers(null, null, false).get(0);
    beerDTO.setVersion(null);
    beerDTO.setId(null);

    given(beerService.saveNewBeer(any(BeerDTO.class))).willReturn(
        beerServiceImpl.listBeers(null, null, false).get(1));

    mockMvc.perform(post(BEER_PATH)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(beerDTO)))
        .andExpect(status().isCreated())
        .andExpect(header().exists("Location"));
  }

  @Test
  void testCreateNewBeerNullBeerName() throws Exception {
    BeerDTO beerDTO = BeerDTO.builder().build();

    given(beerService.saveNewBeer(any(BeerDTO.class))).willReturn(
        beerServiceImpl.listBeers(null, null, false).get(1));

    MvcResult mvcResult = mockMvc.perform(post(BEER_PATH)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(beerDTO)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.length()", is(6)))
        .andReturn();

    System.out.println(mvcResult.getResponse().getContentAsString());
  }


  @Test
  void testListBeers() throws Exception {
    given(beerService.listBeers(any(), any(), any())).willReturn(beerServiceImpl.listBeers(null, null,
        false));

    mockMvc.perform(get(BEER_PATH)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.length()", is(3)));
  }

  @Test
  void getBeerById() throws Exception {
    BeerDTO testBeerDTO = beerServiceImpl.listBeers(null, null, false).get(0);

    given(beerService.getBeerById(any(UUID.class))).willReturn(Optional.of(testBeerDTO));

    mockMvc.perform(get(BEER_PATH_ID, UUID.randomUUID())
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id", is(testBeerDTO.getId().toString())));
  }

  @Test
  void getBeerByIdNotFound() throws Exception {

    given(beerService.getBeerById(any(UUID.class))).willReturn(Optional.empty());

    mockMvc.perform(get(BEER_PATH_ID, UUID.randomUUID())
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

}