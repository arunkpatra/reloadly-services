package com.reloadly.sms.exception;

import com.reloadly.commons.exceptions.ReloadlyException;

/**
 * An exception reserved exclusively for handling errors related to short message service(SMS) processing.
 *
 * @author Arun Patra
 */
public class SMSProcessingException extends ReloadlyException {

    public SMSProcessingException(Throwable cause) {
        super(cause);
    }

}
