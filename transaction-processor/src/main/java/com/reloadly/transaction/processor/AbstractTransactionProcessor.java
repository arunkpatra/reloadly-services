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

package com.reloadly.transaction.processor;

import com.reloadly.autoconfig.notification.service.ReloadlyNotification;
import com.reloadly.commons.exceptions.NotificationException;
import com.reloadly.commons.model.EmailRequest;
import com.reloadly.commons.model.ReloadlyCredentials;
import com.reloadly.commons.model.SmsRequest;
import com.reloadly.commons.model.account.AccountInfo;
import com.reloadly.tracing.utils.TracingUtils;
import com.reloadly.transaction.config.TransactionProcessorProperties;
import com.reloadly.transaction.entity.TransactionEntity;
import com.reloadly.transaction.exception.AccountInfoRetrievalException;
import com.reloadly.transaction.model.TransactionStatus;
import com.reloadly.transaction.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * An abstract transaction processor implementing shared functionality for transaction processors.
 *
 * @author Arun Patra
 */
public abstract class AbstractTransactionProcessor implements TransactionProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractTransactionProcessor.class);
    protected final TransactionProcessorProperties properties;
    protected final RestTemplate restTemplate;
    protected final ApplicationContext context;
    private final TransactionRepository transactionRepository;
    private final ReloadlyNotification notification;

    protected AbstractTransactionProcessor(TransactionProcessorProperties properties, RestTemplate restTemplate,
                                           TransactionRepository transactionRepository,
                                           ReloadlyNotification notification,
                                           ApplicationContext context) {
        this.properties = properties;
        this.restTemplate = restTemplate;
        this.transactionRepository = transactionRepository;
        this.notification = notification;
        this.context = context;
    }

    protected void markTransactionAsProcessing(TransactionEntity txnEntity) {

        // Change status
        txnEntity.setTransactionStatus(TransactionStatus.PROCESSING);
        transactionRepository.save(txnEntity);
    }

    protected void markTransactionAsSuccessful(TransactionEntity txnEntity) {
        txnEntity.setTransactionStatus(TransactionStatus.SUCCESSFUL);
        transactionRepository.save(txnEntity);

    }

    protected void markTransactionAsFailed(TransactionEntity txnEntity) {
        txnEntity.setTransactionStatus(TransactionStatus.FAILED);
        transactionRepository.save(txnEntity);
    }

    protected HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("RELOADLY-API-KEY", properties.getSvcAccountApiKey());
        headers.set("RELOADLY-CLIENT-ID", properties.getClientId());
        headers.set("Content-Type", "application/json");
        headers.set("Accept", "application/json");
        return headers;
    }

    protected ReloadlyCredentials getReloadlyCredentials() {
        ReloadlyCredentials credentials = new ReloadlyCredentials();
        credentials.setType(ReloadlyCredentials.CredentialType.API_KEY);
        credentials.setClientId(properties.getClientId());
        credentials.setCredentials(properties.getSvcAccountApiKey());
        return credentials;
    }

    protected AccountInfo getAccountInfo(String uid) throws AccountInfoRetrievalException {
        Assert.hasLength(uid, "UID can not be empty");

        // Exchange
        try {
            ResponseEntity<AccountInfo> response = restTemplate.exchange(
                    properties.getReloadlyAccountServiceEndpoint().concat("/account/".concat(uid)), HttpMethod.GET,
                    new HttpEntity<>(TracingUtils.getPropagatedHttpHeaders(getHeaders(), context)), AccountInfo.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                LOGGER.info("Account Info obtained");
                return response.getBody();
            } else {
                throw new AccountInfoRetrievalException("Account info fetch failed. Status code: "
                        .concat(response.getStatusCode().toString()));
            }
        } catch (RestClientException e) {
            throw new AccountInfoRetrievalException("An error occurred. Root cause: " + e.getMessage());
        }
    }

    protected void sendEmailMessage(AccountInfo accountInfo, String subject, String body) {
        try {
            notification.sendEmail(getReloadlyCredentials(), new EmailRequest(accountInfo.getEmail(), subject, body));
        } catch (NotificationException e) {
            LOGGER.error("Notification service call failed for email, root cause: " + e.getMessage());
            // Ignore
        }
    }

    protected void sendSMSMessage(AccountInfo accountInfo, String message) {
        try {
            notification.sendSms(getReloadlyCredentials(), new SmsRequest(accountInfo.getPhoneNumber(), message));
        } catch (NotificationException e) {
            LOGGER.error("Notification service call failed for sms, root cause: " + e.getMessage());
            // Ignore
        }
    }

}
