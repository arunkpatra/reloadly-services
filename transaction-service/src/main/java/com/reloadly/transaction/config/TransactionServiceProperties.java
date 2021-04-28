package com.reloadly.transaction.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * The Transaction Microservice Config properties.
 *
 * @author Arun Patra
 */

@ConfigurationProperties(TransactionServiceProperties.PREFIX)
public class TransactionServiceProperties {

    public static final String PREFIX = "reloadly.api.transaction.service";

    /**
     * Kafka properties.
     */
    @NestedConfigurationProperty
    private KafkaProperties kafka;

    public KafkaProperties getKafka() {
        return kafka;
    }

    public void setKafka(KafkaProperties kafka) {
        this.kafka = kafka;
    }

}
