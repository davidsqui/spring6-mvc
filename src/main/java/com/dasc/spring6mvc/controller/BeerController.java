package com.dasc.spring6mvc.controller;

import com.dasc.spring6mvc.model.Beer;
import com.dasc.spring6mvc.service.BeerService;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

@Slf4j
@AllArgsConstructor
@Controller
public class BeerController {

  private final BeerService beerService;

  public Beer getBeerById(UUID id) {
    log.debug("Geet Beer by id in controller");
    return beerService.getBeerById(id);
  }

}
