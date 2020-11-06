package com.sam.repo.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.rsocket.RSocketSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.messaging.handler.invocation.reactive.AuthenticationPrincipalArgumentResolver;
import org.springframework.security.rsocket.core.PayloadSocketAcceptorInterceptor;

@Configuration
public class RSocket {

  @Bean
  PayloadSocketAcceptorInterceptor interceptor(RSocketSecurity security) {
    return security
        .simpleAuthentication(Customizer.withDefaults())
        .authorizePayload(ap -> ap.anyExchange().authenticated())
        .build();
  }

  @Bean
  MapReactiveUserDetailsService authentication() {
    return new MapReactiveUserDetailsService(
        User.withDefaultPasswordEncoder().username("jlong").password("pw").roles("USER").build());
  }

  @Bean
  RSocketMessageHandler messageHandler(RSocketStrategies strategies) {
    var rmh = new RSocketMessageHandler();
    rmh.getArgumentResolverConfigurer()
        .addCustomResolver(new AuthenticationPrincipalArgumentResolver());
    rmh.setRSocketStrategies(strategies);
    return rmh;
  }

}
