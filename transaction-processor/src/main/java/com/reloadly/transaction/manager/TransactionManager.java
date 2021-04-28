package com.reloadly.transaction.manager;

import com.reloadly.transaction.entity.TransactionEntity;
import com.reloadly.transaction.exception.ReloadlyTxnProcessingException;
import com.reloadly.transaction.processor.TransactionProcessor;

/**
 * The transaction manager is a faćade into the configured transaction processors. Each type of transaction can only
 * by handled by a specific {@link TransactionProcessor}.
 * <p>
 * The transaction manager locates the appropriate transaction processor and delegates.
 *
 * @author Arun Patra
 */
public interface TransactionManager {

    /**
     * Delegates to a {@link TransactionProcessor}. If the transaction processor throws an exception, the operation is
     * rolled back. This operation thus must start a database transaction, or possibly a distributed transaction is XA
     * is enabled.
     *
     * @param txnEntity The transaction entity record.
     */
    void handleTransaction(TransactionEntity txnEntity) throws ReloadlyTxnProcessingException;
}
