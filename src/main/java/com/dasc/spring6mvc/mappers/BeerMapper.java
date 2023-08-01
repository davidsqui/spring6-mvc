package com.dasc.spring6mvc.mappers;

import com.dasc.spring6mvc.entities.Beer;
import com.dasc.spring6mvc.model.BeerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface BeerMapper {

  Beer beerDtoToBeer(BeerDTO dto);

  BeerDTO beerToBeerDto(Beer beer);

}
