package com.reloadly.account.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public class AccountDebitRequest {

    private final Float amount;

    @JsonCreator
    public AccountDebitRequest(Float amount) {
        this.amount = amount;
    }

    public Float getAmount() {
        return amount;
    }
}
