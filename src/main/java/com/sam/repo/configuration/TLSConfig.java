package com.sam.repo.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class TLSConfig {

  @Value("${mongo.client.trustStore}")
  private String trustStore;

  @Value("${mongo.client.trustStorePassword}")
  private String trustStorePassword;

  @Value("${mongo.client.keyStore}")
  private String keyStore;

  @Value("${mongo.client.keyStorePassword}")
  private String keyStorePassword;

  @PostConstruct
  private void configureTLS() {
    /*System.setProperty("javax.net.ssl.trustStore", trustStore);
    System.setProperty("javax.net.ssl.trustStorePassword", trustStorePassword);
    System.setProperty("javax.net.ssl.keyStore", keyStore);
    System.setProperty("javax.net.ssl.keyStorePassword", keyStorePassword);
    System.setProperty("jdk.tls.client.protocols", "TLSv1.2");*/
  }
}
