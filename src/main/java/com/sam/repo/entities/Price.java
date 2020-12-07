package com.sam.repo.entities;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class Price {

    private String title;

    private String description;

    private BigDecimal amount;

}
