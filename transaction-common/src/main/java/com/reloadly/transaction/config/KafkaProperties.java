package com.reloadly.transaction.config;

/**
 * Kafka configuration properties.
 *
 * @author Arun Patra
 */
public class KafkaProperties {

    private static final String DEFAULT_TRANSACTION_POSTING_TOPIC = "com.reloadly.inbound.txn.topic";

    /**
     * The inbound transaction posting topic.
     */
    private String inboundTransactionsTopic = DEFAULT_TRANSACTION_POSTING_TOPIC;

    public String getInboundTransactionsTopic() {
        return inboundTransactionsTopic;
    }

    public void setInboundTransactionsTopic(String inboundTransactionsTopic) {
        this.inboundTransactionsTopic = inboundTransactionsTopic;
    }
}
