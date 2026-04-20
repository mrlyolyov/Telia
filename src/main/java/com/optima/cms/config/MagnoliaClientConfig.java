package com.optima.cms.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class MagnoliaClientConfig {

    @Value("${magnolia.base.url}")
    private String baseUrl;

    @Value("${magnolia.usr}")
    private String username;

    @Value("${magnolia.pass}")
    private String password;

    @Bean
    public WebClient magnoliaWebClient() {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeaders(headers -> headers.setBasicAuth(username, password))
                .build();
    }
}