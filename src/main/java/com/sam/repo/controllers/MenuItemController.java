package com.sam.repo.controllers;

import com.github.javafaker.Faker;
import com.sam.repo.entities.MenuItem;
import com.sam.repo.entities.MenuItemDTO;
import com.sam.repo.repositories.MenuItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/menuItems")
public class MenuItemController {

  Converter<String, String> itemTypeCodeConverter =
      new Converter<String, String>() {
        public String convert(MappingContext<String, String> context) {
          return context.getSource().toUpperCase();
        }
      };
  PropertyMap<MenuItem, MenuItemDTO> skipModifiedFieldsMap =
      new PropertyMap<>() {
        protected void configure() {
          using(itemTypeCodeConverter).map(source.getTitle()).setTitle(null);
          // map().setDescription(source.getTitle());
          // map().setTitle(source.getDescription());
        }
      };
  private MenuItemRepository menuItemRepository;
  private ModelMapper modelMapper;

  @Autowired
  public MenuItemController(MenuItemRepository menuRepository) {
    this.menuItemRepository = menuRepository;
    modelMapper = new ModelMapper();
    // modelMapper.addMappings(skipModifiedFieldsMap);
    modelMapper.addMappings(skipModifiedFieldsMap);
  }

  @GetMapping()
  Flux<MenuItem> getAll() {
    return this.menuItemRepository.findAll();
  }

  @GetMapping("/{id}")
  Mono<MenuItemDTO> get(@PathVariable("id") String id) {

    Mono<MenuItem> menuItemMono = this.menuItemRepository.findById(id);
    return menuItemMono.map(
        menuItem -> {
          MenuItemDTO menuItemDTO = modelMapper.map(menuItem, MenuItemDTO.class);
          return menuItemDTO;
        });
  }

  @GetMapping("/fakeTest")
  MenuItemDTO get() {

    Faker faker = new Faker(new Locale("ES"));
    MenuItemDTO menuItemDTO =
        MenuItemDTO.builder()
            .id(UUID.randomUUID().toString())
            .title(faker.food().dish())
            .description(faker.gameOfThrones().quote())
            .price(new BigDecimal(faker.number().randomDouble(2, 1,10)))
            .build();

    return menuItemDTO;
  }

  @PostMapping()
  public Mono<Void> add(@RequestBody Mono<MenuItem> menuItemMono) {
    return this.menuItemRepository.insert(menuItemMono).then();
  }

  @PutMapping()
  public Mono<Void> update(@RequestBody Mono<MenuItem> menuItemMono) {
    return this.menuItemRepository.saveAll(menuItemMono).then();
  }
}
