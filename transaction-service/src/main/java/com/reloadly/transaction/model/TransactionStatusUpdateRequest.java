package com.reloadly.transaction.model;
/**
 * The transaction status update request.
 *
 * @author Arun Patra
 */
public class TransactionStatusUpdateRequest {

    private final TransactionStatus transactionStatus;

    public TransactionStatusUpdateRequest(TransactionStatus transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public TransactionStatus getTransactionStatus() {
        return transactionStatus;
    }
}
