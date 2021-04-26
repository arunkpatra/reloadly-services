package com.reloadly.account.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public class AccountCreditResponse {

    private final String message;

    @JsonCreator
    public AccountCreditResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
