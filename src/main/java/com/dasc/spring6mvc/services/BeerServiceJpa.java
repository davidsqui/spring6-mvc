package com.dasc.spring6mvc.services;

import com.dasc.spring6mvc.entities.Beer;
import com.dasc.spring6mvc.mappers.BeerMapper;
import com.dasc.spring6mvc.model.BeerDTO;
import com.dasc.spring6mvc.repositories.BeerRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Primary
@RequiredArgsConstructor
public class BeerServiceJpa implements BeerService {

  private final BeerRepository beerRepository;
  private final BeerMapper beerMapper;

  @Override
  public List<BeerDTO> listBeers() {
    return beerRepository.findAll().stream()
        .map(beerMapper::beerToBeerDto)
        .collect(Collectors.toList());
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