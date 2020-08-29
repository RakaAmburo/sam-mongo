package com.sam.repo.entities;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Document(collection = "restaurants")
public class Restaurant {

    @Id
    private String id;

    private String name;

    private String address;

}
