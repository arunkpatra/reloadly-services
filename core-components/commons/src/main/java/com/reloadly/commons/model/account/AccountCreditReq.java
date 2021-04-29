package com.reloadly.commons.model.account;

import com.fasterxml.jackson.annotation.JsonCreator;

public class AccountCreditReq {
    private Float amount;

    @JsonCreator
    public AccountCreditReq(Float amount) {
        this.amount = amount;
    }

    public AccountCreditReq() {
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }
}
