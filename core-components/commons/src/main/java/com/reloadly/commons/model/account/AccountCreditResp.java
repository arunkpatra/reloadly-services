package com.reloadly.commons.model.account;

import com.fasterxml.jackson.annotation.JsonCreator;

public class AccountCreditResp {
    private String message;

    @JsonCreator
    public AccountCreditResp(String message) {
        this.message = message;
    }

    public AccountCreditResp() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
