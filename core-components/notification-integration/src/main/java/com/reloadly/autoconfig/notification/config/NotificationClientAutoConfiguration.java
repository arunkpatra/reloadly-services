package com.reloadly.autoconfig.notification.config;

import com.reloadly.autoconfig.notification.service.ReloadlyNotification;
import com.reloadly.autoconfig.notification.service.ReloadlyNotificationImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
/**
 * Notification service integration auto-configuration class.
 *
 * @author Arun Patra
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(NotificationClientProperties.class)
@ConditionalOnProperty(name = {"reloadly.notification.enabled"}, matchIfMissing = true)
public class NotificationClientAutoConfiguration {

    private final NotificationClientProperties properties;

    public NotificationClientAutoConfiguration(NotificationClientProperties properties) {
        this.properties = properties;
    }

    @Bean
    @ConditionalOnMissingBean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ReloadlyNotification reloadlyNotification(NotificationClientProperties properties, RestTemplate restTemplate) {
        return new ReloadlyNotificationImpl(properties, restTemplate);
    }
}
