package com.sam.repo.controllers;

import com.sam.repo.entities.MenuItem;
import com.sam.repo.repositories.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/menuItems")
public class MenuController {

  private MenuRepository menuRepository;

  @Autowired
  public MenuController(MenuRepository menuRepository) {
    this.menuRepository = menuRepository;
  }

  @GetMapping()
  Flux<MenuItem> getItemList() {
    return this.menuRepository.findAll();
  }

  @GetMapping("/{id}")
  Mono<MenuItem> getItem(@PathVariable("id") UUID id) {
    return this.menuRepository.findById(id);
  }

}
