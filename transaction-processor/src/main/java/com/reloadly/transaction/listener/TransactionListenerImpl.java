package com.reloadly.transaction.listener;

import com.reloadly.transaction.service.TransactionProcessorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;

/**
 * Listener to handle incoming transaction messages on a Kafka topic.
 *
 * @author Arun Patra
 */
@Service
public class TransactionListenerImpl implements TransactionListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionListenerImpl.class);
    private final TransactionProcessorService transactionProcessorService;

    public TransactionListenerImpl(TransactionProcessorService transactionProcessorService) {
        this.transactionProcessorService = transactionProcessorService;
    }

    /**
     * Listens for inbound messages in a kafka topic.
     *
     * @param txnId          The transaction ID to be processed.
     * @param messageHeaders The message headers
     */
    @Override
    @KafkaListener(topics = "com.reloadly.inbound.txn.topic", groupId = "inboundTxnProcessingConsumerGrp")
    public void listen(String txnId, MessageHeaders messageHeaders) {
        LOGGER.info("Message received. Txn ID: {}", txnId);
        transactionProcessorService.processInboundTransaction(txnId, messageHeaders);
    }
}
