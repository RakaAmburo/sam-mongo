package com.sam.repo.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

// promotions
@Getter
@Setter
@Document(collection = "groups")
public class Group {

    @Id private String id;

    private String title;

    private String parentId;

    private String description;

}
