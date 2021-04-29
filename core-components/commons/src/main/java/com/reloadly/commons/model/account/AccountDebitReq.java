package com.reloadly.commons.model.account;

import com.fasterxml.jackson.annotation.JsonCreator;

public class AccountDebitReq {
    private Float amount;

    @JsonCreator
    public AccountDebitReq(Float amount) {
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
