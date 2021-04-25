package com.reloadly.transaction.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@EnableConfigurationProperties(TransactionServiceProperties.class)
public class TransactionServiceConfig {

    private final TransactionServiceProperties properties;

    public TransactionServiceConfig(TransactionServiceProperties properties) {
        this.properties = properties;
    }

    @Bean
    public NewTopic topic() {
        return TopicBuilder.name("com.reloadly.inbound.txn.topic")
                .partitions(2)
                .replicas(1)
                .build();
    }

//    @Bean
//    public ProducerFactory<String, String> producerFactory(){
//        Map<String, Object> config = new HashMap<>();
//        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getKafka().getBootstrapServers());
//        return new DefaultKafkaProducerFactory<>(config);
//
//    }

//    @Bean
//    public KafkaAdmin admin() {
//        Map<String, Object> configs = new HashMap<>();
//        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getKafka().getBootstrapServers());
//        return new KafkaAdmin(configs);
//    }

//    @Bean
//    public KafkaTemplate<String, String> kafkaTemplate(ProducerFactory<String, String> producerFactory ) {
//        return new KafkaTemplate<>(producerFactory);
//    }
}
