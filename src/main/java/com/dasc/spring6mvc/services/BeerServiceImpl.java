package com.dasc.spring6mvc.services;

import com.dasc.spring6mvc.model.BeerDTO;
import com.dasc.spring6mvc.model.BeerStyle;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
public class BeerServiceImpl implements BeerService {

  private Map<UUID, BeerDTO> beerMap;

  public BeerServiceImpl() {
    this.beerMap = new HashMap<>();

    BeerDTO beerDTO1 = BeerDTO.builder()
        .id(UUID.randomUUID())
        .version(1)
        .beerName("Galaxy Cat")
        .beerStyle(BeerStyle.PALE_ALE)
        .upc("12356")
        .price(new BigDecimal("12.99"))
        .quantityOnHand(122)
        .createdDate(LocalDateTime.now())
        .updateDate(LocalDateTime.now())
        .build();

    BeerDTO beerDTO2 = BeerDTO.builder()
        .id(UUID.randomUUID())
        .version(1)
        .beerName("Crank")
        .beerStyle(BeerStyle.PALE_ALE)
        .upc("12356222")
        .price(new BigDecimal("11.99"))
        .quantityOnHand(392)
        .createdDate(LocalDateTime.now())
        .updateDate(LocalDateTime.now())
        .build();

    BeerDTO beerDTO3 = BeerDTO.builder()
        .id(UUID.randomUUID())
        .version(1)
        .beerName("Sunshine City")
        .beerStyle(BeerStyle.IPA)
        .upc("12356")
        .price(new BigDecimal("13.99"))
        .quantityOnHand(144)
        .createdDate(LocalDateTime.now())
        .updateDate(LocalDateTime.now())
        .build();

    beerMap.put(beerDTO1.getId(), beerDTO1);
    beerMap.put(beerDTO2.getId(), beerDTO2);
    beerMap.put(beerDTO3.getId(), beerDTO3);
  }

  @Override
  public List<BeerDTO> listBeers() {
    return new ArrayList<>(beerMap.values());
  }

  @Override
  public Optional<BeerDTO> getBeerById(UUID id) {
    log.debug("Get Beer ID was called");
    return Optional.of(beerMap.get(id));
  }

  @Override
  public BeerDTO saveNewBeer(BeerDTO beerDTO) {
    BeerDTO savedBeerDTO = BeerDTO.builder()
        .id(UUID.randomUUID())
        .createdDate(LocalDateTime.now())
        .updateDate(LocalDateTime.now())
        .beerName(beerDTO.getBeerName())
        .beerStyle(beerDTO.getBeerStyle())
        .quantityOnHand(beerDTO.getQuantityOnHand())
        .upc(beerDTO.getUpc())
        .price(beerDTO.getPrice())
        .build();

    beerMap.put(savedBeerDTO.getId(), savedBeerDTO);

    return savedBeerDTO;
  }

  @Override
  public void updateBeerById(UUID beerId, BeerDTO beerDTO) {
    BeerDTO existing = beerMap.get(beerId);
    existing.setBeerName(beerDTO.getBeerName());
    existing.setBeerStyle(beerDTO.getBeerStyle());
    existing.setPrice(beerDTO.getPrice());
    existing.setUpc(beerDTO.getUpc());
    existing.setQuantityOnHand(beerDTO.getQuantityOnHand());
    existing.setVersion(beerDTO.getVersion());
    existing.setUpdateDate(LocalDateTime.now());

    beerMap.put(existing.getId(), existing);
  }

  @Override
  public Boolean deleteBeerById(UUID beerId) {
    beerMap.remove(beerId);
    return true;
  }

  @Override
  public Optional<BeerDTO> patchBeerById(UUID beerId, BeerDTO beer) {
    BeerDTO existing = beerMap.get(beerId);

    if (StringUtils.hasText(beer.getBeerName())) {
      existing.setBeerName(beer.getBeerName());
    }

    if (beer.getBeerStyle() != null) {
      existing.setBeerStyle(beer.getBeerStyle());
    }

    if (beer.getPrice() != null) {
      existing.setPrice(beer.getPrice());
    }

    if (beer.getQuantityOnHand() != null) {
      existing.setQuantityOnHand(beer.getQuantityOnHand());
    }

    if (StringUtils.hasText(beer.getUpc())) {
      existing.setUpc(beer.getUpc());
    }

    return Optional.of(existing);
  }
}
