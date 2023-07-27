package com.dasc.spring6mvc.service;

import com.dasc.spring6mvc.model.Beer;
import java.util.UUID;

public interface BeerService {

  Beer getBeerById(UUID id);

}
