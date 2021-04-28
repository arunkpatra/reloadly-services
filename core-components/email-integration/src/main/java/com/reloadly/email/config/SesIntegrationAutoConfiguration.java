package com.reloadly.email.config;

import com.reloadly.email.service.SimpleEmailServiceMailSender;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.SesClientBuilder;

/**
 * Email auto-configuration class.
 *
 * @author Arun Patra
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(SesProperties.class)
@ConditionalOnProperty(name = "reloadly.integration.email.ses.enabled", matchIfMissing = true)
public class SesIntegrationAutoConfiguration {

    private final SesProperties properties;

    public SesIntegrationAutoConfiguration(SesProperties properties) {
        this.properties = properties;
    }

    @Bean
    @ConditionalOnMissingClass("javax.mail.Session")
    public MailSender simpleMailSender(SesClient sesClient, SesProperties properties) {
        return new SimpleEmailServiceMailSender(sesClient, properties);
    }

    @Bean
    @ConditionalOnMissingBean
    public SesClient sesClient() {
        SesClientBuilder sesV2ClientBuilder = SesClient.builder();
        sesV2ClientBuilder.region(Region.of(properties.getRegion()));
        return sesV2ClientBuilder.build();
    }

}
