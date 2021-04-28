package com.reloadly.transaction.model;

/**
 * The transaction status.
 *
 * @author Arun Patra
 */
public enum TransactionStatus {

    /**
     * The transaction has been accepted for processing.
     */
    ACCEPTED,
    /**
     * The transaction has been rejected. Can not be retried.
     */
    REJECTED,
    /**
     * The transaction is currently being processed.
     */
    PROCESSING,
    /**
     * Transaction processing was successful.
     */
    SUCCESSFUL,
    /**
     * Transaction processing has failed. May be retried.
     */
    FAILED,
    /**
     * Transaction processing was cancelled. May be retried.
     */
    CANCELLED
}
