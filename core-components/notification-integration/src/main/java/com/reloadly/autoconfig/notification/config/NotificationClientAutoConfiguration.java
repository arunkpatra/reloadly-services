package com.reloadly.autoconfig.notification.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(NotificationClientProperties.class)
public class NotificationClientAutoConfiguration {


}
