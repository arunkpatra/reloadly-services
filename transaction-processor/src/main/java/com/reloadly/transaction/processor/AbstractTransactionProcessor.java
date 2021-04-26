package com.reloadly.transaction.processor;

import com.reloadly.commons.model.ReloadlyCredentials;
import com.reloadly.transaction.config.TransactionProcessorProperties;
import com.reloadly.transaction.processor.TransactionProcessor;
import org.springframework.http.HttpHeaders;

public abstract class AbstractTransactionProcessor implements TransactionProcessor {

    protected final TransactionProcessorProperties properties;

    protected AbstractTransactionProcessor(TransactionProcessorProperties properties) {
        this.properties = properties;
    }

    protected HttpHeaders getHeaders(ReloadlyCredentials credentials) {
        HttpHeaders  headers = new HttpHeaders();
        headers.set("RELOADLY-API-KEY", properties.getSvcAccountApiKey());
        return headers;
    }
}
