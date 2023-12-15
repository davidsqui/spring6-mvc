package com.dasc.spring6mvc.model;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerDTO {

  private UUID id;
  private String name;
  private String email;
  private Integer version;
  private LocalDateTime createdDate;
  private LocalDateTime updateDate;

}
