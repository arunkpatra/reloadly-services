package com.reloadly.transaction.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public class AddMoneyRequest {

    private final Float amount;

    @JsonCreator
    public AddMoneyRequest(Float amount) {
        this.amount = amount;
    }

    public Float getAmount() {
        return amount;
    }
}
