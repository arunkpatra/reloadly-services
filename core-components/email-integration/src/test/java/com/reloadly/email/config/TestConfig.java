package com.reloadly.email.config;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.services.ses.SesClient;

@Configuration
public class TestConfig {

    @Bean
    @Primary
    @Profile("mock-ses-client")
    public SesClient sesClient() {
        return Mockito.mock(SesClient.class);
    }
}
