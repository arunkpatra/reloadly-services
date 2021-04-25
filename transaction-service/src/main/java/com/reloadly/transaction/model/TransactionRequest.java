package com.reloadly.transaction.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public class TransactionRequest {

    private final TransactionType transactionType;
    private final AddMoneyRequest addMoneyRequest;
    private final SendAirtimeRequest sendAirtimeRequest;

    @JsonCreator
    public TransactionRequest(TransactionType transactionType, AddMoneyRequest addMoneyRequest, SendAirtimeRequest sendAirtimeRequest) {
        this.transactionType = transactionType;
        this.addMoneyRequest = addMoneyRequest;
        this.sendAirtimeRequest = sendAirtimeRequest;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public AddMoneyRequest getAddMoneyRequest() {
        return addMoneyRequest;
    }

    public SendAirtimeRequest getSendAirtimeRequest() {
        return sendAirtimeRequest;
    }
}
