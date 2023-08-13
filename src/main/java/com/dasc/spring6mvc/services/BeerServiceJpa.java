package com.dasc.spring6mvc.services;

import com.dasc.spring6mvc.entities.Beer;
import com.dasc.spring6mvc.mappers.BeerMapper;
import com.dasc.spring6mvc.model.BeerDTO;
import com.dasc.spring6mvc.model.BeerStyle;
import com.dasc.spring6mvc.repositories.BeerRepository;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Primary
@RequiredArgsConstructor
public class BeerServiceJpa implements BeerService {

  private final BeerRepository beerRepository;
  private final BeerMapper beerMapper;

  private static final int DEFAULT_PAGE = 0;
  private static final int DEFAULT_PAGE_SIZE = 25;

  @Override
  public Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory,
      Integer pageNumber, Integer pageSize) {

    PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);

    Page<Beer> beers;
    if (StringUtils.hasText(beerName) && beerStyle == null) {
      beers = listBeersByName(beerName, pageRequest);
    } else if (!StringUtils.hasText(beerName) && beerStyle != null) {
      beers = listBeersByStyle(beerStyle, pageRequest);
    } else if (StringUtils.hasText(beerName) && beerStyle != null) {
      beers = listBeersByNameAndStyle(beerName, beerStyle, pageRequest);
    } else {
      beers = beerRepository.findAll(pageRequest);
    }

    if (showInventory != null && showInventory) {
      beers.forEach(beer -> beer.setQuantityOnHand(null));
    }

    return beers.map(beerMapper::beerToBeerDto);
  }

  private PageRequest buildPageRequest(Integer pageNumber, Integer pageSize) {
    int queryPageNumber;
    int queryPageSize;

    if (pageNumber != null && pageNumber > 0) {
      queryPageNumber = pageNumber - 1;
    } else {
      queryPageNumber = DEFAULT_PAGE;
    }

    if (pageSize == null) {
      queryPageSize = DEFAULT_PAGE_SIZE;
    } else {
      if (pageSize > 1000) {
        queryPageSize = 1000;
      } else {
        queryPageSize = pageSize;
      }
    }

    return PageRequest.of(queryPageNumber, queryPageSize);
  }

  private Page<Beer> listBeersByName(String beerName, Pageable pageable) {
    return beerRepository.findAllByBeerNameIsLikeIgnoreCase("%" + beerName + "%", pageable);
  }

  private Page<Beer> listBeersByStyle(BeerStyle beerStyle, Pageable pageable) {
    return beerRepository.findAllByBeerStyle(beerStyle, pageable);
  }

  private Page<Beer> listBeersByNameAndStyle(String beerName, BeerStyle beerStyle,
      Pageable pageable) {
    return beerRepository.findAllByBeerNameIsLikeIgnoreCaseAndBeerStyle("%" + beerName + "%",
        beerStyle, pageable);
  }

  @Override
  public Optional<BeerDTO> getBeerById(UUID id) {
    return beerRepository.findById(id)
        .map(beerMapper::beerToBeerDto);
  }

  @Override
  public BeerDTO saveNewBeer(BeerDTO beerDTO) {
    Beer beer = beerRepository.save(beerMapper.beerDtoToBeer(beerDTO));
    return beerMapper.beerToBeerDto(beer);
  }

  @Override
  public void updateBeerById(UUID beerId, BeerDTO beerDTO) {
    beerRepository.findById(beerId).ifPresent(foundBeer -> {
      foundBeer.setBeerName(beerDTO.getBeerName());
      foundBeer.setBeerStyle(beerDTO.getBeerStyle());
      foundBeer.setUpc(beerDTO.getUpc());
      foundBeer.setPrice(beerDTO.getPrice());
      beerRepository.save(foundBeer);
    });
  }

  @Override
  public Boolean deleteBeerById(UUID beerId) {
    if (beerRepository.existsById(beerId)) {
      beerRepository.deleteById(beerId);
      return true;
    }
    return false;
  }

  @Override
  public Optional<BeerDTO> patchBeerById(UUID beerId, BeerDTO beer) {
    AtomicReference<Optional<BeerDTO>> atomicReference = new AtomicReference<>();

    beerRepository.findById(beerId).ifPresentOrElse(foundBeer -> {
      if (StringUtils.hasText(beer.getBeerName())) {
        foundBeer.setBeerName(beer.getBeerName());
      }
      if (beer.getBeerStyle() != null) {
        foundBeer.setBeerStyle(beer.getBeerStyle());
      }
      if (StringUtils.hasText(beer.getUpc())) {
        foundBeer.setUpc(beer.getUpc());
      }
      if (beer.getPrice() != null) {
        foundBeer.setPrice(beer.getPrice());
      }
      if (beer.getQuantityOnHand() != null) {
        foundBeer.setQuantityOnHand(beer.getQuantityOnHand());
      }
      atomicReference.set(Optional.of(beerMapper
          .beerToBeerDto(beerRepository.save(foundBeer))));
    }, () -> {
      atomicReference.set(Optional.empty());
    });

    return atomicReference.get();
  }
}
