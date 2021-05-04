package com.reloadly.transaction.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The Send Airtime request.
 *
 * @author Arun Patra
 */
public class SendAirtimeRequest {
    private final Float amount;
    private final String phoneNumber;

    @JsonCreator
    public SendAirtimeRequest(@JsonProperty("amount") Float amount, @JsonProperty("phoneNumber") String phoneNumber) {
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
