/*
 * MIT License
 *
 * Copyright (c) 2021 Arun Patra
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.reloadly.account.service;

import com.reloadly.account.entity.AccountEntity;
import com.reloadly.autoconfig.notification.service.ReloadlyNotification;
import com.reloadly.commons.exceptions.NotificationException;
import com.reloadly.commons.model.EmailRequest;
import com.reloadly.commons.model.SmsRequest;
import com.reloadly.security.utils.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides supporting functions for notification sending.
 */
public abstract class AccountUpdateNotificationSupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountUpdateNotificationSupport.class);
    private final ReloadlyNotification notification;

    protected AccountUpdateNotificationSupport(ReloadlyNotification notification) {
        this.notification = notification;
    }

    protected void sendAccountUpdateNotifications(AccountEntity ae, boolean newAccount) {
        // Send email
        try {
            notification.sendEmail(SecurityUtils.getCredentials(), createEmailRequest(ae, newAccount));
        } catch (NotificationException e) {
            LOGGER.error("Failed to send email notification. Root cause: " + e.getMessage());
        }

        // send sms
        try {
            notification.sendSms(SecurityUtils.getCredentials(), createSmsRequest(ae, newAccount));
        } catch (NotificationException e) {
            LOGGER.error("Failed to send email notification. Root cause: " + e.getMessage());
        }
    }

    private EmailRequest createEmailRequest(AccountEntity ae, boolean newAccount) {
        if (newAccount) {
            return new EmailRequest(ae.getEmail(), "Reloadly Account Created", "Congratulations! Your " +
                    "reloadly account has been created. Enjoy! \n Regards, \n Team Reloadly");
        } else {
            return new EmailRequest(ae.getEmail(), "Reloadly Account Updated", "Your " +
                    "reloadly account has been updated.  \n Regards, \n Team Reloadly");
        }
    }

    private SmsRequest createSmsRequest(AccountEntity ae, boolean newAccount) {
        if (newAccount) {
            return new SmsRequest(ae.getPhoneNumber(), "Congratulations! Your " +
                    "reloadly account has been created. Enjoy! \n Regards, \n Team Reloadly");
        } else {
            return new SmsRequest(ae.getPhoneNumber(), "Your " +
                    "reloadly account has been updated.  \n Regards, \n Team Reloadly");
        }
    }
}
