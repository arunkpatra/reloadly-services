package com.reloadly.security.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public class HelloResponse {

    private final String message;

    @JsonCreator
    public HelloResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
