package com.reloadly.commons.model.account;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountDebitReq {
    private Float amount;

    @JsonCreator
    public AccountDebitReq(@JsonProperty("amount") Float amount) {
        this.amount = amount;
    }

    public AccountDebitReq() {
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }
}
