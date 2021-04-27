package com.reloadly.transaction.service;

import com.reloadly.transaction.exception.ReloadlyTxnProcessingException;

public interface TransactionProcessorService {

    /**
     * Process an inbound transaction. Note that, this operation **MUST** be idempotent in nature.
     *
     * @param txnId The transaction ID to be processed.
     */
    void processInboundTransaction(String txnId);
}
