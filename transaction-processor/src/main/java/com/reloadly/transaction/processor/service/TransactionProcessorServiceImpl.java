package com.reloadly.transaction.processor.service;

import com.reloadly.autoconfig.notification.service.ReloadlyNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class TransactionProcessorServiceImpl extends TransactionProcessingSupport implements TransactionProcessorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionProcessorServiceImpl.class);

    protected TransactionProcessorServiceImpl(ReloadlyNotification notification) {
        super(notification);
    }

    @KafkaListener(topics = "com.reloadly.inbound.txn.topic", groupId = "inboundTxnProcessingConsumerGrp")
    public void processInboundTransaction(String txnId) {
        LOGGER.info("Inbound Transaction received. Txn ID: {}", txnId);
    }
}
