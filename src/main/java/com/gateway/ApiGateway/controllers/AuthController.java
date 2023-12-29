package com.gateway.ApiGateway.controllers;

import com.gateway.ApiGateway.models.AuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private Logger logger = LoggerFactory.getLogger(AuthController.class);

    /*
    This is a controller method that handles the login request.
     It gets the OAuth 2.0 client from the registered client map,
     gets the user from the authentication principal,
     and creates a response object with the user's email,
     access token, refresh token, and expiration time.
     The response object is then returned to the client
     */
    @GetMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RegisteredOAuth2AuthorizedClient("okta") OAuth2AuthorizedClient client,
            @AuthenticationPrincipal OidcUser user,
            Model model){



        logger.info("User email id: {}", user.getEmail());
        //Creating response objects
       AuthResponse authResponse = new AuthResponse();

       // setting user id
       authResponse.setUserId(user.getEmail());

       //setting take to auth response
       authResponse.setAccessToken(client.getAccessToken().getTokenValue());

       authResponse.setRefreshToken(client.getRefreshToken().getTokenValue());
       authResponse.setExpireAt(client.getAccessToken().getExpiresAt().getEpochSecond());

      List<String> authorities =  user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());

      authResponse.setAuthority(authorities);

      return new ResponseEntity<>(authResponse, HttpStatus.OK);

    }
}
