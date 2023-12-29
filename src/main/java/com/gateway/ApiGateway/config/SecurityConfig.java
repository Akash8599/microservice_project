package com.gateway.ApiGateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    /**
 * Configures the security filters and authorization rules for the server.
 * Sets up OAuth2 login and resource server using JWT for authentication and authorization.
 */
@Bean
public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity serverHttpSecurity){
    serverHttpSecurity
            .authorizeExchange()
            .anyExchange()
            .authenticated()
            .and()
            .oauth2Login()
            .and()
            .oauth2ResourceServer()
            .jwt();
    return serverHttpSecurity.build();
}
}
