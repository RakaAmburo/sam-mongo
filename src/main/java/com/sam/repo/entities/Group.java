package com.sam.repo.entities;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

// promotions
@Getter
@Document(collection = "groups")
public class Group {

    @Id private String id;

    private String title;

    private String parentId;

    private String description;

}
