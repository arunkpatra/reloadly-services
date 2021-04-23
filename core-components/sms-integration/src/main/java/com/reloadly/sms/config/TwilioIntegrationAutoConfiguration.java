package com.reloadly.sms.config;

import com.reloadly.sms.service.SMSService;
import com.reloadly.sms.service.SMSServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(TwilioProperties.class)
@ConditionalOnProperty(name = "meelu.integration.sms.twilio", havingValue = "true", matchIfMissing = true)
public class TwilioIntegrationAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(SMSService.class)
    SMSService smsService(TwilioProperties properties) {
        return new SMSServiceImpl(properties);
    }
}
