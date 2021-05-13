package com.reloadly.commons.model.account;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountDebitResp {
    private final Boolean successful;
    private final String message;

    @JsonCreator
    public AccountDebitResp(@JsonProperty("successful") Boolean successful, @JsonProperty("message") String message) {
        this.successful = successful;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public Boolean getSuccessful() {
        return successful;
    }
}
