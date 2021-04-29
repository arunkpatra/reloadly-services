package com.reloadly.notification.config;

import com.reloadly.notification.service.NotificationService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Configuration
public class TestConfig {

    @Bean
    @Primary
    @Profile("mocked-notification-svc")
    public NotificationService notificationService() {
        return Mockito.mock(NotificationService.class);
    }
}
