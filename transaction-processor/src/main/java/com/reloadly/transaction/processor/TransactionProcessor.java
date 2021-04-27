package com.reloadly.transaction.processor;

import com.reloadly.transaction.entity.TransactionEntity;
import com.reloadly.transaction.exception.ReloadlyTxnProcessingException;

public interface TransactionProcessor {

    /**
     * Depending on the context, this operation must be performed atomically. Consider using a database transaction.
     * It is the responsibility of the processor to cleanly rollback if needed.
     *
     * @param txnEntity
     * @throws ReloadlyTxnProcessingException If an exception occurs.
     */
    void handleTransaction(TransactionEntity txnEntity) throws ReloadlyTxnProcessingException;
}
