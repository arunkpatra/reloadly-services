package com.reloadly.transaction.service;

import com.reloadly.transaction.exception.ReloadlyTxnException;
import com.reloadly.transaction.model.TransactionRequest;
import com.reloadly.transaction.model.TransactionResponse;

public interface TransactionService {

    /**
     * Accepts or rejects a transaction initiated by the caller. Note that, the transaction response is
     * returned immediately. However, the transaction is picked up by the Transaction Processor component and
     * processed as needed. Their is no guarantee that a transaction eventually succeeds, but whatever the result,
     * the caller is necessarily notified.
     *
     * The caller can always query back the status of a transaction.
     * @param request The transaction request object.
     * @return A transaction response object.
     * @throws ReloadlyTxnException If the transaction could not be handled.
     *
     * @author Arun Patra
     */
    TransactionResponse createNewTxn(TransactionRequest request) throws ReloadlyTxnException;
}
