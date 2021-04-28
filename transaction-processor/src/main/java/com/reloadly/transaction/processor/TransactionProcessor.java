package com.reloadly.transaction.processor;

import com.reloadly.transaction.annotation.TransactionHandler;
import com.reloadly.transaction.entity.TransactionEntity;
import com.reloadly.transaction.exception.ReloadlyTxnProcessingException;
import com.reloadly.transaction.manager.TransactionManager;
import com.reloadly.transaction.model.TransactionType;

/**
 * Service Provider Interface (SPI) for a transaction processor.
 * <p>
 * A transaction processor handles a specific {@link TransactionType} of transaction. It must throw a
 * {@link ReloadlyTxnProcessingException} in which case, the {@link TransactionManager} will roll back any operations
 * performed by resources(e.g. database) provided they are transaction aware and are participating in the transaction.
 * <p>
 * Distributed transaction handling is a complex topic and has far reaching consequences on system scalability. Hence,
 * follow transaction management best practices while implementing this SPI.
 *<p>
 * Concrete implementations of this SPI must apply the {@link TransactionHandler} annotation at the type level.
 *
 * @author Arun Patra
 */
public interface TransactionProcessor {

    /**
     * Process a transaction.
     *
     * @param txnEntity The transaction record.
     * @throws ReloadlyTxnProcessingException If an exception occurs.
     */
    void processTransaction(TransactionEntity txnEntity) throws ReloadlyTxnProcessingException;
}
