package com.sam.repo.entities;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@Document(collection = "menuItems")
public class MenuItem {

    @Id
    private UUID id;

    private String title;

    private String description;

    private BigDecimal price;


}
