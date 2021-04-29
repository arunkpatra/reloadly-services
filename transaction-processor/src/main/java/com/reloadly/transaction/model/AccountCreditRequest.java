package com.reloadly.transaction.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public class AccountCreditRequest {
    private Float amount;

    @JsonCreator
    public AccountCreditRequest(Float amount) {
        this.amount = amount;
    }

    public AccountCreditRequest() {
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }
}
