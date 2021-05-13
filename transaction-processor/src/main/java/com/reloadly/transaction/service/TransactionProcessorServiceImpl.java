package com.reloadly.transaction.service;

import com.reloadly.tracing.annotation.Traced;
import com.reloadly.transaction.entity.TransactionEntity;
import com.reloadly.transaction.exception.ReloadlyTxnProcessingException;
import com.reloadly.transaction.manager.TransactionManager;
import com.reloadly.transaction.model.TransactionStatus;
import com.reloadly.transaction.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Optional;

@Service
public class TransactionProcessorServiceImpl implements TransactionProcessorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionProcessorServiceImpl.class);
    private final TransactionRepository transactionRepository;
    private final TransactionManager transactionManager;

    protected TransactionProcessorServiceImpl(TransactionRepository transactionRepository,
                                              TransactionManager transactionManager) {
        this.transactionRepository = transactionRepository;
        this.transactionManager = transactionManager;
    }

    @Traced(operationName = "TransactionProcessor#processInboundTransaction")
    public void processInboundTransaction(String txnId, MessageHeaders messageHeaders) {
        Assert.notNull(txnId, "Transaction ID can not be null");

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
        try {
            transactionManager.handleTransaction(txnEntity);
        } catch (ReloadlyTxnProcessingException e) {
            LOGGER.error("Error while processing transaction with txnID: {}. Root Cause: {}", txnId, e.getMessage());
        }
    }
}
