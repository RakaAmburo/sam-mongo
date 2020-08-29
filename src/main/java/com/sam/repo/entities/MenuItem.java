package com.sam.repo.entities;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Document(collection = "menuItems")
@CompoundIndexes({
  @CompoundIndex(name = "rest_lang", def = "{'restaurantId' : 1, 'languageId': 1}", unique = true)
})
public class MenuItem {

  // encuadrado presentacion sobresaltado
  // imadenes varias para todo
  // sort by gropu should gropu be indexed

  @Id private String id;

  private String restaurantId;

  private String languageId;

  private List<Group> groups;

  private String title;

  private String description;

  private List<Price> prices;

}
