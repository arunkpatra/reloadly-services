package com.reloadly.transaction.service;

import com.reloadly.tracing.utils.TracingUtils;
import com.reloadly.transaction.config.TransactionServiceProperties;
import com.reloadly.transaction.entity.TransactionEntity;
import com.reloadly.transaction.exception.KafkaProcessingException;
import com.reloadly.transaction.model.TransactionRequest;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

/**
 * This provides Kafka support.
 *
 * @author Arun Patra
 */
public abstract class KafkaSupport {

    protected final TransactionServiceProperties properties;
    private final KafkaTemplate<String, String> template;
    private final ApplicationContext context;

    public KafkaSupport(TransactionServiceProperties properties, KafkaTemplate<String, String> template,
                        ApplicationContext context) {
        this.properties = properties;
        this.template = template;
        this.context = context;
    }

    protected void postTransactionToKafka(TransactionEntity te, TransactionRequest request) throws KafkaProcessingException {
        String topic = properties.getKafka().getInboundTransactionsTopic();
        try {
            Message<String> message = new Message<String>() {
                @Override
                public String getPayload() {
                    return te.getTxnId();
                }

                @Override
                public MessageHeaders getHeaders() {
                    return TracingUtils.getPropagatedMessageHeaders(context);
                }
            };
            template.setDefaultTopic(topic);
            template.send(message);
//            template.send(topic, te.getTxnId());
        } catch (Exception e) {
            throw new KafkaProcessingException("Failed to post transaction to Kafka. Root cause: ".concat(e.getMessage()), e);
        }
    }
}
