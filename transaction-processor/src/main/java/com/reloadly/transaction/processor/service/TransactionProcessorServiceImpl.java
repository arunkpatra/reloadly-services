package com.reloadly.transaction.processor.service;

import com.reloadly.transaction.entity.TransactionEntity;
import com.reloadly.transaction.exception.ReloadlyTxnProcessingException;
import com.reloadly.transaction.model.TransactionStatus;
import com.reloadly.transaction.repository.TransactionRepository;
import com.reloadly.transaction.service.TransactionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Optional;

@Service
public class TransactionProcessorServiceImpl  implements TransactionProcessorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionProcessorServiceImpl.class);
    private final TransactionRepository transactionRepository;
    private final TransactionManager transactionManager;

    protected TransactionProcessorServiceImpl(TransactionRepository transactionRepository,
                                              TransactionManager transactionManager) {
        this.transactionRepository = transactionRepository;
        this.transactionManager = transactionManager;
    }

    @Transactional
    @KafkaListener(topics = "com.reloadly.inbound.txn.topic", groupId = "inboundTxnProcessingConsumerGrp")
    public void processInboundTransaction(String txnId) throws ReloadlyTxnProcessingException {
        Assert.notNull(txnId, "Transaction ID can not be null");
        LOGGER.info("Inbound Transaction received. Txn ID: {}", txnId);

        Optional<TransactionEntity> txnOpt = transactionRepository.findByTxnId(txnId);
        if (!txnOpt.isPresent()) {
            LOGGER.error("Transaction ID: {} was not found.", txnId);
            return;
        }

        TransactionEntity txnEntity = txnOpt.get();

        // Reject if in one of the following states
        TransactionStatus txnStatus = txnEntity.getTransactionStatus();
        if (txnStatus.equals(TransactionStatus.PROCESSING) || txnStatus.equals(TransactionStatus.SUCCESSFUL)) {
            LOGGER.warn("Transaction ID: {} is has an incompatible state {}. Exiting.", txnId, txnStatus.name());
            return;
        }

        // Delegate to the transaction manager
        transactionManager.handleTransaction(txnEntity);

    }
}
