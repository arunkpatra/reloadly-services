package com.reloadly.commons.exceptions;

import com.reloadly.commons.exceptions.ReloadlyException;

public class NotificationException extends ReloadlyException {

    private static final String DEFAULT_MESSAGE = "Notification service error occurred.";

    public NotificationException() {
        super(DEFAULT_MESSAGE);
    }

    public NotificationException(Exception e) {
        super(DEFAULT_MESSAGE, e);
    }

    public NotificationException(String msg) {
        super(msg);
    }

    public NotificationException(String message, Exception e) {
        super(message, e);
    }

}
