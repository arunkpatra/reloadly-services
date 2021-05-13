package com.reloadly.commons.model.account;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountCreditReq {
    private final Float amount;

    @JsonCreator
    public AccountCreditReq(@JsonProperty("amount") Float amount) {
        this.amount = amount;
    }

    public Float getAmount() {
        return amount;
    }
}
