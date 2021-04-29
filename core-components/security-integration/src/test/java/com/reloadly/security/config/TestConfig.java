package com.reloadly.security.config;

import com.reloadly.security.service.ReloadlyAuth;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

@Configuration
public class TestConfig {

    @Bean
    @Primary
    @Profile("mock-authsvc")
    public ReloadlyAuth reloadlyAuth() {
        return Mockito.mock(ReloadlyAuth.class);
    }

    @Bean
    @Primary
    @Profile("mock-rest-template")
    public RestTemplate restTemplate() {
        return Mockito.mock(RestTemplate.class);
    }
}
