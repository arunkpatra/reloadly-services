package com.reloadly.transaction.processor.service;


import com.reloadly.transaction.entity.TransactionEntity;
import com.reloadly.transaction.model.TransactionStatus;
import com.reloadly.transaction.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class TransactionProcessingSupport extends TransactionProcessingNotificationSupport{

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionProcessingSupport.class);
    private final TransactionRepository transactionRepository;

    protected TransactionProcessingSupport(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    protected void handleAddMoneyTransaction(TransactionEntity te) {
        // Change status
        te.setTransactionStatus(TransactionStatus.PROCESSING);
        te = transactionRepository.save(te);

        //
        // TODO: Ask the Account microservice to credit the account
        //

        te.setTransactionStatus(TransactionStatus.SUCCESSFUL);
        te = transactionRepository.save(te);

    }


    protected void handleSendAirtimeTransaction(TransactionEntity te) {

    }
}
