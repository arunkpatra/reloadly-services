package com.reloadly.sms.exception;

import com.reloadly.commons.exceptions.ReloadlyException;

/**
 * An exception reserved exclusively for handling errors related to short message service(SMS) processing.
 *
 * @author Arun Patra
 */
public class SMSProcessingException extends ReloadlyException {
    public SMSProcessingException() {
    }

    public SMSProcessingException(String message) {
        super(message);
    }

    public SMSProcessingException(String message, Throwable cause) {
        super(message, cause);
    }

    public SMSProcessingException(Throwable cause) {
        super(cause);
    }

    public SMSProcessingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
