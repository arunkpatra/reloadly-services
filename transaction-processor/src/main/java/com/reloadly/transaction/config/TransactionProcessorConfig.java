package com.reloadly.transaction.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@EnableConfigurationProperties(TransactionProcessorProperties.class)
public class TransactionProcessorConfig {

    private final TransactionProcessorProperties properties;

    public TransactionProcessorConfig(TransactionProcessorProperties properties) {
        this.properties = properties;
    }

//    @Bean
//    public NewTopic topic() {
//        return TopicBuilder.name("com.reloadly.inbound.txn.topic")
//                .partitions(2)
//                .replicas(1)
//                .build();
//    }
}
