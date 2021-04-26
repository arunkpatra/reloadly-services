package com.reloadly.transaction.processor;

import com.reloadly.transaction.entity.TransactionEntity;
import com.reloadly.transaction.exception.ReloadlyTxnProcessingException;

public interface TransactionProcessor {

    void handleTransaction(TransactionEntity txnEntity) throws ReloadlyTxnProcessingException;
}
