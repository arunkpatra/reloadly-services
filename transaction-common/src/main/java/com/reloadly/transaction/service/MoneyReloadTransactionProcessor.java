package com.reloadly.transaction.service;

import com.reloadly.transaction.annotation.TransactionHandler;
import com.reloadly.transaction.config.TransactionServiceProperties;
import com.reloadly.transaction.entity.MoneyReloadTxnEntity;
import com.reloadly.transaction.entity.TransactionEntity;
import com.reloadly.transaction.exception.ReloadlyTxnProcessingException;
import com.reloadly.transaction.model.TransactionStatus;
import com.reloadly.transaction.repository.MoneyReloadTxnRepository;
import com.reloadly.transaction.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@TransactionHandler(value = "ADD_MONEY", description = "Transaction processor to hand money reload")
public class MoneyReloadTransactionProcessor extends AbstractTransactionProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(MoneyReloadTransactionProcessor.class);
    private final TransactionRepository transactionRepository;
    private final MoneyReloadTxnRepository moneyReloadTxnRepository;

    public MoneyReloadTransactionProcessor(TransactionServiceProperties transactionServiceProperties,
                                           TransactionRepository transactionRepository,
                                           MoneyReloadTxnRepository moneyReloadTxnRepository) {
        super(transactionServiceProperties);
        this.transactionRepository = transactionRepository;
        this.moneyReloadTxnRepository = moneyReloadTxnRepository;
    }

    @Override
    public void handleTransaction(TransactionEntity txnEntity) throws ReloadlyTxnProcessingException {

        LOGGER.info("Handling money reload transaction.");
        // Change status
        txnEntity.setTransactionStatus(TransactionStatus.PROCESSING);
        txnEntity = transactionRepository.save(txnEntity);

        // It is assumed that, actual monies have already been debited from a payer account via some payment processor
        // Now credit account. Account can only be credited by the Account Microservice.
        try {
            creditAccount(txnEntity);
        } catch (Exception e) {
            throw new ReloadlyTxnProcessingException("failed to credit account. Root cause: ".concat(e.getMessage()), e);
        }

        txnEntity.setTransactionStatus(TransactionStatus.SUCCESSFUL);
        transactionRepository.save(txnEntity);
        LOGGER.info("Money reload successful.");

    }

    private void creditAccount(TransactionEntity txnEntity) throws Exception {
        String uid = txnEntity.getUid();
        Optional<MoneyReloadTxnEntity> mrTxnOpt = moneyReloadTxnRepository.getByTxnId(txnEntity.getTxnId());
        if (!mrTxnOpt.isPresent()) {
            throw new ReloadlyTxnProcessingException(String.format("Money reload transaction with ID: %s was not found", txnEntity.getTxnId()));
        }
        Float amount = mrTxnOpt.get().getAmount();
        // Now make call


    }
}
