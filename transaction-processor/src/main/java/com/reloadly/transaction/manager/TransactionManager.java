package com.reloadly.transaction.manager;

import com.reloadly.transaction.entity.TransactionEntity;
import com.reloadly.transaction.exception.ReloadlyTxnProcessingException;

public interface TransactionManager {

    /**
     * This should always complete normally.
     * @param txnEntity
     */
    void handleTransaction(TransactionEntity txnEntity) throws ReloadlyTxnProcessingException;
}
