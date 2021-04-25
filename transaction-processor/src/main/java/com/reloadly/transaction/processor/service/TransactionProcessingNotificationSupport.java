package com.reloadly.transaction.processor.service;


import com.reloadly.autoconfig.notification.service.ReloadlyNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class TransactionProcessingNotificationSupport {

    private final ReloadlyNotification notification;
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionProcessingNotificationSupport.class);

    protected TransactionProcessingNotificationSupport(ReloadlyNotification notification) {
        this.notification = notification;
    }

}
