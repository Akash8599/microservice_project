package com.user.service.config;

import com.user.service.config.interceptor.RestTemplateInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * This file contains the configuration for the service.
 * It declares a bean for RestTemplate with @LoadBalanced annotation.
 * The RestTemplate bean is used for making HTTP requests.
 */
@Configuration
public class ServiceConfig {

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    @Autowired
    private OAuth2AuthorizedClientRepository oAuth2AuthorizedClientRepository;
    /**
     * Creates a RestTemplate bean.
     * The @LoadBalanced annotation enables client-side load balancing.
     * @return RestTemplate - the RestTemplate bean
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(){

        RestTemplate restTemplate = new RestTemplate();
        List<ClientHttpRequestInterceptor> interceptorList = new ArrayList<>();
        interceptorList.add(new RestTemplateInterceptor(manager(clientRegistrationRepository, oAuth2AuthorizedClientRepository)));
        restTemplate.setInterceptors(interceptorList);
        return restTemplate;
    }

       /**
     * Creates and configures an OAuth2AuthorizedClientManager.
     * @param clientRegistrationRepository The repository for client registrations.
     * @param auth2AuthorizedClientRepository The repository for authorized clients.
     * @return The configured OAuth2AuthorizedClientManager.
     */
    @Bean
    public OAuth2AuthorizedClientManager manager(ClientRegistrationRepository clientRegistrationRepository,
                                                 OAuth2AuthorizedClientRepository auth2AuthorizedClientRepository){

        DefaultOAuth2AuthorizedClientManager defaultOAuth2AuthorizedClientManager = new DefaultOAuth2AuthorizedClientManager(clientRegistrationRepository, auth2AuthorizedClientRepository);
        OAuth2AuthorizedClientProvider provider = OAuth2AuthorizedClientProviderBuilder.builder().clientCredentials().build();
        defaultOAuth2AuthorizedClientManager.setAuthorizedClientProvider(provider);

        return defaultOAuth2AuthorizedClientManager;
    }
}
