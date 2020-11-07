package com.sam.repo.controllers;

import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.time.Duration;
import java.util.stream.Stream;

@Controller
@Log4j2
public class ControllingRSocket {

    @MessageMapping("startPing")
    Flux<String> startPing() {


        Flux<String> pingSignal =
                Flux.fromStream(Stream.generate(() -> "ping")).delayElements(Duration.ofMillis(1000));


        return pingSignal;
    }

    @MessageMapping("mongoChannel")
    Flux<BigRequest> channel(RSocketRequester clientRSocketConnection, Flux<BigRequest> bigRequestFlux) {
        System.out.println("llegamos al mongo");
        return Flux.create(
                (FluxSink<BigRequest> sink) -> {
                    bigRequestFlux
                            .doOnNext(
                                    i -> {
                                        System.out.println(i.getId());
                                        sink.next(i);
                                    })
                            .subscribe();
                });
    }
}
