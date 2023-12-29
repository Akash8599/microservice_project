package com.user.service.config.interceptor;


import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.stereotype.Component;

/**
 * This class is responsible for intercepting Feign client requests and adding an OAuth2 access token to the request header.
 */
@Configuration
@Component
public class FeignClientInterceptor implements RequestInterceptor {


    /**
     * This class is an implementation of Feign Client Interceptor that adds an OAuth2 access token to the request header.
     * It uses an OAuth2AuthorizedClientManager to obtain the access token for the specified client registration ID and principal.
     */
    @Autowired
    private OAuth2AuthorizedClientManager manager;

    @Override
    public void apply(RequestTemplate requestTemplate){
        String token = manager.authorize(OAuth2AuthorizeRequest.withClientRegistrationId("my-internal-client").principal("internal")
                .build()).getAccessToken().getTokenValue();
        requestTemplate.header("Authorization", "Bearer " + token);
    }
}
