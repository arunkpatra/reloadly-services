package com.reloadly.transaction.processor.service;

import com.reloadly.transaction.exception.ReloadlyTxnProcessingException;

public interface TransactionProcessorService {

    /**
     * Process an inbound transaction. Note that, this operation **MUST** be idempotent in nature.
     *
     * @param txnId The transaction ID to be processed.
     * @throws ReloadlyTxnProcessingException If an exception occurs.
     */
    void processInboundTransaction(String txnId) throws ReloadlyTxnProcessingException;
}
