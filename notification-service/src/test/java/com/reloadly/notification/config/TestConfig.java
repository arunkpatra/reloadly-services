package com.reloadly.notification.config;

import com.reloadly.notification.service.NotificationService;
import com.reloadly.sms.service.SMSService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.MailSender;

@Configuration
public class TestConfig {

    @Bean
    @Primary
    @Profile("mocked-notification-svc")
    public NotificationService notificationService() {
        return Mockito.mock(NotificationService.class);
    }

    @Bean
    @Primary
    @Profile("mocked-mail-sender")
    public MailSender mailSender() {
        return Mockito.mock(MailSender.class);
    }

    @Bean
    @Primary
    @Profile("mocked-sms-service")
    public SMSService sMSService() {
        return Mockito.mock(SMSService.class);
    }

}
