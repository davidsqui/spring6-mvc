package com.dasc.spring6mvc.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class BeerDTO {

  private UUID id;
  private Integer version;

  @NotBlank
  @NotNull
  private String beerName;

  @NotNull
  private BeerStyle beerStyle;

  @NotNull
  @NotBlank
  private String upc;
  private Integer quantityOnHand;

  @NotNull
  private BigDecimal price;

  private LocalDateTime createdDate;

  private LocalDateTime updateDate;

}
