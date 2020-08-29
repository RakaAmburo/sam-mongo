package com.sam.repo.entities;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class Price {

    private String title;

    private String description;

    private BigDecimal amount;

}
