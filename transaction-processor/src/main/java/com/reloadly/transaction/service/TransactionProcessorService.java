package com.reloadly.transaction.service;

import org.springframework.messaging.MessageHeaders;

public interface TransactionProcessorService {

    /**
     * Process an inbound transaction. Note that, this operation **MUST** be idempotent in nature.
     *
     * @param txnId The transaction ID to be processed.
     */
    void processInboundTransaction(String txnId, MessageHeaders messageHeaders);
}
