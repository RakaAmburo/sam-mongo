package com.sam.repo.entities;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Document(collection = "languages")
public class Languages {

  @Id private String id;

  private String language;

}
