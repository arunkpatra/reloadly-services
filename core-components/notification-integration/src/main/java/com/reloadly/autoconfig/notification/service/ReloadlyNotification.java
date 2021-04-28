package com.reloadly.autoconfig.notification.service;

import com.reloadly.commons.exceptions.NotificationException;
import com.reloadly.commons.model.EmailRequest;
import com.reloadly.commons.model.ReloadlyCredentials;
import com.reloadly.commons.model.SmsRequest;
/**
 * Java API to interact with the Notification microservice.
 *
 * @author Arun Patra
 */
public interface ReloadlyNotification {

    /**
     * Routes an email request to the notification microservice.
     *
     * @param credentials The credentials object containing a valid Reloadly Auth Service issued JWT token or a similar
     *                    identifier which identifies the user under whose authority the API call will be made.
     * @param request The email request object.
     * @throws NotificationException If an error occurs.
     */
    void sendEmail(ReloadlyCredentials credentials, EmailRequest request) throws NotificationException;

    /**
     * Routes a SMS request to the notification microservice.
     *
     * @param credentials The credentials object containing a valid Reloadly Auth Service issued JWT token or a similar
     *                    identifier which identifies the user under whose authority the API call will be made.
     * @param request The SMS request object.
     * @throws NotificationException If an error occurs.
     */
    void sendSms(ReloadlyCredentials credentials, SmsRequest request) throws NotificationException;
}
