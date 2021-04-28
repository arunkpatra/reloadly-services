package com.reloadly.commons.model;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Notification response.
 *
 * @author Arun Patra
 */
public class NotificationResponse {

    private String message;

    @JsonCreator
    public NotificationResponse(String message) {
        this.message = message;
    }

    public NotificationResponse() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
