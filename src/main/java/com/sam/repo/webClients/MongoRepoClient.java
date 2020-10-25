package com.sam.repo.webClients;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sam.repo.entities.MenuItem;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.util.retry.Retry;
import reactor.util.retry.RetryBackoffSpec;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class MongoRepoClient {

  private WebClient webClient;
  private ObjectMapper objectMapper;

  private String destHost;
  private String destPort;
  private String destBasePath;
  private String menuItemPath;
  private int timeOut;
  private int timeoutBackOff;

  private RetryBackoffSpec unAuthBackOffStrategy;
  private RetryBackoffSpec timeBackOffStrategy;

  @Autowired
  public MongoRepoClient(
      final WebClient.Builder webClientBuilder,
      @Value("${mongoRepoClient.host:http://localhost}") final String destHost,
      @Value("${mongoRepoClient.port:8080}") final String destPort,
      @Value("${mongoRepoClient.basePath:/sam-repo}") final String destBasePath,
      @Value("${mongoRepoClient.menuItemPath:/menuItems}") final String menuItemPath,
      @Value("${mongoRepoClient.timeOut:6000}") final int timeOut,
      @Value("${mongoRepoClient.timeoutBackOff:200}") final int timeoutBackOff) {

    this.destHost = destHost;
    this.destPort = destPort;
    this.destBasePath = destBasePath;
    this.menuItemPath = menuItemPath;
    this.timeOut = timeOut;
    this.timeoutBackOff = timeoutBackOff;

    this.timeBackOffStrategy = getConnectionTimeOutBackoffStrategy(this.timeoutBackOff);

    HttpClient httpClient =
        HttpClient.create()
            .tcpConfiguration(
                client ->
                    client
                        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, this.timeOut)
                        .doOnConnected(
                            conn ->
                                conn.addHandlerLast(
                                        new ReadTimeoutHandler(this.timeOut, TimeUnit.MILLISECONDS))
                                    .addHandlerLast(
                                        new WriteTimeoutHandler(
                                            this.timeOut, TimeUnit.MILLISECONDS))));

    ClientHttpConnector connector = new ReactorClientHttpConnector(httpClient);

    String baseUrl = String.format("%s:%s%s", this.destHost, this.destPort, this.destBasePath);

    webClient = webClientBuilder.baseUrl(baseUrl).clientConnector(connector).build();
    objectMapper = new ObjectMapper();
  }

  private RetryBackoffSpec getConnectionTimeOutBackoffStrategy(Integer millis) {
    return Retry.backoff(3, Duration.ofMillis(millis))
        .jitter(0.5d)
        .filter(
            errorFilter -> {
              boolean returnValue = false;
              if (errorFilter.getClass().equals(ReadTimeoutException.class)) {
                returnValue = true;
              }
              return returnValue;
            })
        .onRetryExhaustedThrow(
            (retryBackoffSpec, retrySignal) ->
                new MongoRepoClientRetryExhaustedException(
                    "retry exhausted for MongoRepoClient due to timeout"));
  }

  public void getMenuItemList() {

    webClient
        .get()
        .uri(this.menuItemPath)
        .retrieve()
        .bodyToFlux(MenuItem.class)
        .retryWhen(this.timeBackOffStrategy)
        .doOnNext(element -> log.info(element.getTitle()))
        .subscribe();
  }
}
