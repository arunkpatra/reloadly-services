package com.reloadly.transaction.service;

import com.reloadly.transaction.config.TransactionServiceProperties;
import com.reloadly.transaction.entity.TransactionEntity;
import com.reloadly.transaction.exception.KafkaProcessingException;
import com.reloadly.transaction.model.TransactionRequest;
import org.springframework.kafka.core.KafkaTemplate;

public abstract class KafkaSupport {

    protected final TransactionServiceProperties properties;
    private final KafkaTemplate<String, String> template;

    public KafkaSupport(TransactionServiceProperties properties, KafkaTemplate<String, String> template) {
        this.properties = properties;
        this.template = template;
    }

    protected void postTransactionToKafka(TransactionEntity te, TransactionRequest request) throws KafkaProcessingException {
        String topic = properties.getKafka().getInboundTransactionsTopic();
        try {
            template.send(topic, te.getTxnId());
        } catch (Exception e) {
            throw new KafkaProcessingException("Failed to post transaction to Kafka. Root cause: ".concat(e.getMessage()), e);
        }
    }
}
