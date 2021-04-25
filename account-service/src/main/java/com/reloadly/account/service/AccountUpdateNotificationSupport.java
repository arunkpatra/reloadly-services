package com.reloadly.account.service;

import com.reloadly.account.entity.AccountEntity;
import com.reloadly.autoconfig.notification.service.ReloadlyNotification;
import com.reloadly.commons.exceptions.NotificationException;
import com.reloadly.commons.model.EmailRequest;
import com.reloadly.commons.model.SmsRequest;
import com.reloadly.security.utils.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AccountUpdateNotificationSupport {

    private final ReloadlyNotification notification;
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountUpdateNotificationSupport.class);

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
