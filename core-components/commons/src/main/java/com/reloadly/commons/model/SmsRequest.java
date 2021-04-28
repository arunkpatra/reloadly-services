package com.reloadly.commons.model;

/**
 * An SMS request.
 *
 * @author Arun Patra
 */
public class SmsRequest {

    private final String phoneNumber;
    private final String message;

    public SmsRequest(String phoneNumber, String message) {
        this.phoneNumber = phoneNumber;
        this.message = message;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getMessage() {
        return message;
    }
}
