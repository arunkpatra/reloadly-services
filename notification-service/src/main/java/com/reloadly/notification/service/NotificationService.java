package com.reloadly.notification.service;

import com.reloadly.notification.exception.NotificationException;
import com.reloadly.commons.model.EmailRequest;
import com.reloadly.commons.model.SmsRequest;

/**
 * Notification service to send emails and SMS.
 *
 * @author Arun Patra
 */
public interface NotificationService {

    /**
     * Send an email.
     * @param request The email request.
     * @throws NotificationException If an exception occurs.
     */
    void sendEmail(EmailRequest request) throws NotificationException;

    /**
     * Send an SMS.
     * @param request The SMS request.
     * @throws NotificationException If an exception occurs.
     */
    void sendSms(SmsRequest request) throws NotificationException;
}
