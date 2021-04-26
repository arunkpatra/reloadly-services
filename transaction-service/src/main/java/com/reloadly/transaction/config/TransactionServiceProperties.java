package com.reloadly.transaction.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@ConfigurationProperties(TransactionServiceProperties.PREFIX)
public class TransactionServiceProperties {

    public static final String PREFIX = "reloadly.api.transaction.service";

    /**
     * The service account API key.
     */
    private String svcAccountApiKey = "";
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

    public String getSvcAccountApiKey() {
        return svcAccountApiKey;
    }

    public void setSvcAccountApiKey(String svcAccountApiKey) {
        this.svcAccountApiKey = svcAccountApiKey;
    }
}
