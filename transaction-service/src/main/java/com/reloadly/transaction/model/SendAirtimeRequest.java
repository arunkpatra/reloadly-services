package com.reloadly.transaction.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public class SendAirtimeRequest {
    private final Float amount;
    private final String phoneNumber;

    @JsonCreator
    public SendAirtimeRequest(Float amount, String phoneNumber) {
        this.amount = amount;
        this.phoneNumber = phoneNumber;
    }

    public Float getAmount() {
        return amount;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
