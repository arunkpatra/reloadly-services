package com.reloadly.transaction.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * The Transaction Processor Config.
 *
 * @author Arun Patra
 */
@Configuration
@EnableConfigurationProperties(TransactionProcessorProperties.class)
public class TransactionProcessorConfig {

    private final TransactionProcessorProperties properties;

    public TransactionProcessorConfig(TransactionProcessorProperties properties) {
        this.properties = properties;
    }

    @Bean
    @ConditionalOnMissingBean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
