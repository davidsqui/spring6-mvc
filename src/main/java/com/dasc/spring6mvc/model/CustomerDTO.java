package com.dasc.spring6mvc.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerDTO {

  private UUID id;
  @NotBlank
  @NotNull
  private String name;
  private String email;
  private Integer version;
  private LocalDateTime createdDate;
  private LocalDateTime updateDate;

}
