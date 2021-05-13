package com.reloadly.commons.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Notification response.
 *
 * @author Arun Patra
 */
public class NotificationResponse {

    private final String message;

    @JsonCreator
    public NotificationResponse(@JsonProperty("message") String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
