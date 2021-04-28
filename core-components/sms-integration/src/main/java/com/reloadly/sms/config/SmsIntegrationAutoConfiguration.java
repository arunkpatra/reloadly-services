package com.reloadly.sms.config;

import com.reloadly.sms.service.SMSService;
import com.reloadly.sms.service.TwilioSMSServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * SMS integration auto-configuration class.
 *
 * @author Arun Patra
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(SmsProperties.class)
@ConditionalOnProperty(name = "reloadly.integration.sms.twilio", matchIfMissing = true)
public class SmsIntegrationAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(SMSService.class)
    SMSService smsService(SmsProperties properties) {
        return new TwilioSMSServiceImpl(properties);
    }
}
