package com.reloadly.transaction.model;

public class TransactionStatusUpdateRequest {

    private final TransactionStatus transactionStatus;

    public TransactionStatusUpdateRequest(TransactionStatus transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public TransactionStatus getTransactionStatus() {
        return transactionStatus;
    }
}
