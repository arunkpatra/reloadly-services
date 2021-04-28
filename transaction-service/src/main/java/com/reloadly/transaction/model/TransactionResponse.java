package com.reloadly.transaction.model;

/**
 * The transaction response.
 *
 * @author Arun Patra
 */
public class TransactionResponse {

    private final String transactionId;
    private final TransactionStatus transactionStatus;

    public TransactionResponse(String transactionId, TransactionStatus transactionStatus) {
        this.transactionId = transactionId;
        this.transactionStatus = transactionStatus;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public TransactionStatus getTransactionStatus() {
        return transactionStatus;
    }
}
