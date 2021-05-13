package com.reloadly.commons.exceptions;

/**
 * Exception indicating notification service errors.
 *
 * @author Arun Patra
 */
public class NotificationException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Notification service error occurred.";

    public NotificationException() {}
    public NotificationException(String msg) {
        super(msg);
    }

    public NotificationException(String message, Exception e) {
        super(message, e);
    }

}
