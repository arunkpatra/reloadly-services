package com.reloadly.transaction.service;

import com.reloadly.transaction.entity.TransactionEntity;
import com.reloadly.transaction.exception.ReloadlyTxnProcessingException;

public interface TransactionManager {

    void handleTransaction(TransactionEntity txnEntity) throws ReloadlyTxnProcessingException;
}
