package com.reloadly.transaction.processor.service;

import com.reloadly.autoconfig.notification.service.ReloadlyNotification;

public abstract class TransactionProcessingSupport extends TransactionProcessingNotificationSupport{
    protected TransactionProcessingSupport(ReloadlyNotification notification) {
        super(notification);
    }
}
