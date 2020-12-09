package com.sam.repo.controllers;

import com.sam.commons.entities.BigRequest;
import com.sam.commons.entities.MenuItemDTO;
import com.sam.commons.entities.MenuItemReq;
import com.sam.commons.entities.Status;
import com.sam.repo.entities.MenuItem;
import com.sam.repo.repositories.MenuItemRepository;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.UnicastProcessor;

import java.time.Duration;
import java.util.stream.Stream;

@Controller
@Log4j2
public class ControllingRSocket {

  private MenuItemRepository menuItemRepository;
  private ModelMapper modelMapper;

  @Autowired
  public ControllingRSocket(MenuItemRepository menuRepository) {
    this.menuItemRepository = menuRepository;
    this.modelMapper = new ModelMapper();
  }

  @MessageMapping("startPing")
  Flux<String> startPing() {

    System.out.println("iniciamos ping");
    Flux<String> pingSignal =
        Flux.fromStream(Stream.generate(() -> "ping")).delayElements(Duration.ofMillis(1000));

    return pingSignal;
  }

  @MessageMapping("mongoChannel")
  Flux<BigRequest> channel(
      RSocketRequester clientRSocketConnection, Flux<BigRequest> bigRequestFlux) {
    System.out.println("arrived to mongo");
    return Flux.create(
        (FluxSink<BigRequest> sink) -> {
          bigRequestFlux
              .doOnNext(
                  i -> {
                    // System.out.println(i.getId());
                    sink.next(i);
                  })
              .subscribe();
        });
  }

  @MessageMapping("menuItemChannel")
  Flux<MenuItemReq> menuItemChannel(Flux<MenuItemReq> menuItemReqFlux) {

    return processMenuItemRequest(menuItemReqFlux);
  }

  @MessageMapping("deleteMenuItemChannel")
  Flux<MenuItemReq> deleteMenuItemChannel(Flux<MenuItemReq> menuItemReqFlux) {

    return processMenuItemRequest(menuItemReqFlux);
  }

  private Flux<MenuItemReq> processMenuItemRequest(Flux<MenuItemReq> menuItemReqFlux) {

    UnicastProcessor<MenuItemReq> responseStream = UnicastProcessor.create();
    FluxSink<MenuItemReq> responseSink = responseStream.sink();

    menuItemReqFlux
        .doOnNext(
            menuItemReq -> {
              // System.out.println(i.getId());
              MenuItem menuItem = modelMapper.map(menuItemReq.getMenuItemDTO(), MenuItem.class);
              switch (menuItemReq.getAction()) {
                case INSERT:
                  this.menuItemRepository
                      .insert(menuItem)
                      .doOnSuccess(
                          menuItemMono -> {
                            menuItemReq.setStatus(Status.OK);
                            menuItemReq.setMenuItemDTO(
                                modelMapper.map(menuItemMono, MenuItemDTO.class));
                            responseSink.next(menuItemReq);
                          })
                      .doOnError(
                          error -> {
                            menuItemReq.setStatus(Status.ERROR);
                          })
                      .subscribe();
                  break;
                case UPDATE:
                  this.menuItemRepository
                      .save(menuItem)
                      .doOnSuccess(
                          menuItemMono -> {
                            menuItemReq.setStatus(Status.OK);
                            menuItemReq.setMenuItemDTO(
                                modelMapper.map(menuItemMono, MenuItemDTO.class));
                            responseSink.next(menuItemReq);
                          })
                      .subscribe();

                case DELETE:
                  this.menuItemRepository
                      .delete(menuItem)
                      .doOnSuccess(
                          menuItemMono -> {
                            menuItemReq.setStatus(Status.OK);
                            responseSink.next(menuItemReq);
                          })
                      .subscribe();
                  break;
              }
              // sink.next(menuItemReq);
            })
        .subscribe();

    return responseStream;
  }
}
