package com.reloadly.transaction.service;

import com.reloadly.commons.model.ReloadlyCredentials;
import com.reloadly.transaction.config.TransactionServiceProperties;
import org.springframework.http.HttpHeaders;

public abstract class AbstractTransactionProcessor implements TransactionProcessor {

    protected final TransactionServiceProperties transactionServiceProperties;

    protected AbstractTransactionProcessor(TransactionServiceProperties transactionServiceProperties) {
        this.transactionServiceProperties = transactionServiceProperties;
    }

    protected HttpHeaders getHeaders(ReloadlyCredentials credentials) {
        HttpHeaders  headers = new HttpHeaders();
        headers.set("RELOADLY-API-KEY", transactionServiceProperties.getSvcAccountApiKey());
        return headers;
    }
}
