package com.reloadly.transaction.processor.service;

import com.reloadly.transaction.entity.TransactionEntity;
import com.reloadly.transaction.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Optional;

@Service
public class TransactionProcessorServiceImpl extends TransactionProcessingSupport implements TransactionProcessorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionProcessorServiceImpl.class);
    private final TransactionRepository transactionRepository;

    protected TransactionProcessorServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @KafkaListener(topics = "com.reloadly.inbound.txn.topic", groupId = "inboundTxnProcessingConsumerGrp")
    public void processInboundTransaction(String txnId) {
        Assert.notNull(txnId, "Transaction ID can not be null");

        LOGGER.info("Inbound Transaction received. Txn ID: {}", txnId);

        Optional<TransactionEntity> txnOpt = transactionRepository.findByTxnId(txnId);

        if (!txnOpt.isPresent()) {
            LOGGER.error("Transaction ID: {} was not found.", txnId);
            return;
        }

    }
}
