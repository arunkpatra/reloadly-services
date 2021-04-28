package com.reloadly.sms.service;


import com.reloadly.sms.exception.SMSProcessingException;

/**
 * A provider agnostic interface to send SMS messages.
 *
 * @author Arun Patra
 */
public interface SMSService {

    /**
     * Send an SMS.
     *
     * @param toNumber The number to which SMS should be sent.
     * @param message  The message to be sent.
     * @throws SMSProcessingException If an error occurs.
     */
    void sendSMS(String toNumber, String message) throws SMSProcessingException;
}
