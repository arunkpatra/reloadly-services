package com.reloadly.transaction.processor;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.reloadly.autoconfig.notification.service.ReloadlyNotification;
import com.reloadly.commons.exceptions.NotificationException;
import com.reloadly.commons.model.EmailRequest;
import com.reloadly.commons.model.ReloadlyCredentials;
import com.reloadly.commons.model.SmsRequest;
import com.reloadly.transaction.config.TransactionProcessorProperties;
import com.reloadly.transaction.entity.TransactionEntity;
import com.reloadly.transaction.exception.AccountInfoRetrievalException;
import com.reloadly.transaction.model.TransactionStatus;
import com.reloadly.transaction.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final TransactionRepository transactionRepository;
    private final ReloadlyNotification notification;

    protected AbstractTransactionProcessor(TransactionProcessorProperties properties, RestTemplate restTemplate, TransactionRepository transactionRepository, ReloadlyNotification notification) {
        this.properties = properties;
        this.restTemplate = restTemplate;
        this.transactionRepository = transactionRepository;
        this.notification = notification;
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
        headers.set("Content-Type", "application/json");
        headers.set("Accept", "application/json");
        return headers;
    }

    protected ReloadlyCredentials getReloadlyCredentials() {
        ReloadlyCredentials credentials = new ReloadlyCredentials();
        credentials.setType(ReloadlyCredentials.CredentialType.API_KEY);
        credentials.setCredentials(properties.getSvcAccountApiKey());
        return credentials;
    }

    protected AccountInfo getAccountInfo(String uid) throws AccountInfoRetrievalException {
        Assert.hasLength(uid, "UID can not be empty");

        // Exchange
        try {
            ResponseEntity<AccountInfo> response = restTemplate.exchange(
                    properties.getReloadlyAccountServiceEndpoint().concat("/account/".concat(uid)), HttpMethod.GET,
                    new HttpEntity<>(getHeaders()), AccountInfo.class);
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
            notification.sendEmail(getReloadlyCredentials(), new EmailRequest(accountInfo.email, subject, body));
        } catch (NotificationException e) {
            LOGGER.error("Notification service call failed for email, root cause: " + e.getMessage());
            // Ignore
        }
    }

    protected void sendSMSMessage(AccountInfo accountInfo, String message) {
        try {
            notification.sendSms(getReloadlyCredentials(), new SmsRequest(accountInfo.phoneNumber, message));
        } catch (NotificationException e) {
            LOGGER.error("Notification service call failed for sms, root cause: " + e.getMessage());
            // Ignore
        }
    }

    protected static class AccountInfo {

        private String name;
        private String email;
        private String phoneNumber;

        @JsonCreator
        public AccountInfo(String name, String email, String phoneNumber) {
            this.name = name;
            this.email = email;
            this.phoneNumber = phoneNumber;
        }

        public AccountInfo() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

    }
}
