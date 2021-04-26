package com.reloadly.transaction.service;

import com.reloadly.transaction.annotation.TransactionHandler;
import com.reloadly.transaction.config.TransactionServiceProperties;
import com.reloadly.transaction.entity.TransactionEntity;
import com.reloadly.transaction.exception.ReloadlyTxnProcessingException;
import com.reloadly.transaction.model.TransactionStatus;
import com.reloadly.transaction.repository.AirtimeSendTxnRepository;
import com.reloadly.transaction.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@TransactionHandler(value = "SEND_AIRTIME", description = "Transaction processor to hand sending airtime")
public class AirTimeSendTransactionProcessor extends AbstractTransactionProcessor{

    private static final Logger LOGGER = LoggerFactory.getLogger(AirTimeSendTransactionProcessor.class);
    private final TransactionRepository transactionRepository;
    private final AirtimeSendTxnRepository airtimeSendTxnRepository;

    public AirTimeSendTransactionProcessor(TransactionServiceProperties transactionServiceProperties,
                                           TransactionRepository transactionRepository,
                                           AirtimeSendTxnRepository airtimeSendTxnRepository) {
        super(transactionServiceProperties);
        this.transactionRepository = transactionRepository;
        this.airtimeSendTxnRepository = airtimeSendTxnRepository;
    }

    @Override
    public void handleTransaction(TransactionEntity txnEntity) throws ReloadlyTxnProcessingException {
        LOGGER.info("Handling airtime send transaction.");

        // Change status
        txnEntity.setTransactionStatus(TransactionStatus.PROCESSING);
        txnEntity = transactionRepository.save(txnEntity);

        // TODO: Send Airtime

        txnEntity.setTransactionStatus(TransactionStatus.SUCCESSFUL);
        transactionRepository.save(txnEntity);
        LOGGER.info("Airtime send successful.");
    }
}
