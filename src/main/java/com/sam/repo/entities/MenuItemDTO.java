package com.sam.repo.entities;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class MenuItemDTO {

    private String id;

    private String title;

    private String description;

    private BigDecimal price;
}
