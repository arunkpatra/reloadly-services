package com.reloadly.transaction.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public class SendAirtimeRequest {
    private final Float amount;

    @JsonCreator
    public SendAirtimeRequest(Float amount) {
        this.amount = amount;
    }

    public Float getAmount() {
        return amount;
    }

}
