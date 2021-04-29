package com.reloadly.commons.model.account;

import com.fasterxml.jackson.annotation.JsonCreator;

public class AccountDebitResp {
    private Boolean successful;
    private String message;

    @JsonCreator
    public AccountDebitResp(Boolean successful, String message) {
        this.successful = successful;
        this.message = message;
    }

    public AccountDebitResp() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getSuccessful() {
        return successful;
    }

    public void setSuccessful(Boolean successful) {
        this.successful = successful;
    }
}
