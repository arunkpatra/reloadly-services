package com.reloadly.transaction.config;

import com.reloadly.autoconfig.notification.service.ReloadlyNotification;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

@Configuration
public class MockConfig {

    @Bean
    @Primary
    public RestTemplate restTemplate() {
        return Mockito.mock(RestTemplate.class);
    }

    @Bean
    @Primary
    public ReloadlyNotification notification() {
        return Mockito.mock(ReloadlyNotification.class);
    }
}
