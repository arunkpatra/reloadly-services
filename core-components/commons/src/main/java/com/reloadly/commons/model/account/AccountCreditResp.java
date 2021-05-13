package com.reloadly.commons.model.account;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountCreditResp {
    private final String message;

    @JsonCreator
    public AccountCreditResp(@JsonProperty("message") String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
