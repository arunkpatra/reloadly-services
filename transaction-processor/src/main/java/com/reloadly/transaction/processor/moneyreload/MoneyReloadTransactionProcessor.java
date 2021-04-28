package com.reloadly.transaction.processor.moneyreload;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.reloadly.autoconfig.notification.service.ReloadlyNotification;
import com.reloadly.transaction.annotation.TransactionHandler;
import com.reloadly.transaction.config.TransactionProcessorProperties;
import com.reloadly.transaction.entity.MoneyReloadTxnEntity;
import com.reloadly.transaction.entity.TransactionEntity;
import com.reloadly.transaction.exception.ReloadlyTxnProcessingException;
import com.reloadly.transaction.processor.AbstractTransactionProcessor;
import com.reloadly.transaction.repository.MoneyReloadTxnRepository;
import com.reloadly.transaction.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

/**
 * An illustrative implementation of a Money Reload transaction processor.
 *
 * @author Arun Patra
 */
@TransactionHandler(value = "ADD_MONEY", description = "Transaction processor to hand money reload")
public class MoneyReloadTransactionProcessor extends AbstractTransactionProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(MoneyReloadTransactionProcessor.class);
    private final MoneyReloadTxnRepository moneyReloadTxnRepository;

    public MoneyReloadTransactionProcessor(TransactionProcessorProperties transactionProcessorProperties,
                                           TransactionRepository transactionRepository,
                                           MoneyReloadTxnRepository moneyReloadTxnRepository,
                                           RestTemplate restTemplate, ReloadlyNotification notification) {
        super(transactionProcessorProperties, restTemplate, transactionRepository, notification);
        this.moneyReloadTxnRepository = moneyReloadTxnRepository;
    }

    @Override
    public void processTransaction(TransactionEntity txnEntity) throws ReloadlyTxnProcessingException {

        LOGGER.info("Handling money reload transaction.");
        markTransactionAsProcessing(txnEntity);

        // It is assumed that, actual monies have already been debited from a payer account via some payment processor
        // If you still need to do some work left, here's the spot.

        // >>>
        // >>> Your business logic goes here.
        // >>>

        // Now credit account. Account can only be credited by the Account Microservice.
        try {
            creditAccount(txnEntity);
        } catch (ReloadlyTxnProcessingException e) {
            markTransactionAsFailed(txnEntity);
            LOGGER.error("Money reload transaction with txnId: {} has failed with reason {}", txnEntity.getTxnId(), e.getMessage());
            sendNotifications(txnEntity.getUid(), false);
        } catch (Exception e) {
            throw new ReloadlyTxnProcessingException("Unhandled exception occurred. Rolling back transaction. Root cause: ".concat(e.getMessage()), e);
        }

        markTransactionAsSuccessful(txnEntity);
        LOGGER.info("Money reload successful.");
        sendNotifications(txnEntity.getUid(), true);
    }

    /**
     * Send notifications
     *
     * @param uid    The UID
     * @param status If the transaction has failed.
     */
    private void sendNotifications(String uid, boolean status) {
        String emailSub = status ? "Money Reloaded Successful" : "Money Reloaded Failed";
        String message = status ? "Your money reload transaction has been processed. Thank you." :
                "Your money reload transaction has failed.";
        try {
            AccountInfo accountInfo = getAccountInfo(uid);
            sendEmailMessage(accountInfo, emailSub, message);
            sendSMSMessage(accountInfo, message);
        } catch (Exception e) {
            // Log and ignore
            LOGGER.error("Error while sending notification. Root cause: {}", e.getMessage());
        }
    }

    private void creditAccount(TransactionEntity txnEntity) throws ReloadlyTxnProcessingException {
        String uid = txnEntity.getUid();
        Optional<MoneyReloadTxnEntity> mrTxnOpt = moneyReloadTxnRepository.getByTxnId(txnEntity.getTxnId());
        if (!mrTxnOpt.isPresent()) {
            throw new ReloadlyTxnProcessingException(String.format("Money reload transaction with ID: %s was not found", txnEntity.getTxnId()));
        }
        Float amount = mrTxnOpt.get().getAmount();
        // Now make call

        Assert.hasLength(uid, "UID can not be empty");
        Assert.notNull(amount, "Amount can not be null");

        // Exchange
        try {
            ResponseEntity<AccountCreditResponse> response =
                    restTemplate.postForEntity(properties.getReloadlyAccountServiceEndpoint().concat("/account/credit/".concat(uid)),
                            new HttpEntity<>(new AccountCreditRequest(amount), getHeaders()),
                            AccountCreditResponse.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                LOGGER.info("Credit transaction successful");
            } else {
                throw new ReloadlyTxnProcessingException("Credit transaction failed. Status code: "
                        .concat(response.getStatusCode().toString()));
            }
        } catch (RestClientException e) {
            throw new ReloadlyTxnProcessingException("An error occurred. Root cause: " + e.getMessage());
        }
    }

    private static class AccountCreditRequest {
        private Float amount;

        @JsonCreator
        public AccountCreditRequest(Float amount) {
            this.amount = amount;
        }

        public AccountCreditRequest() {
        }

        public Float getAmount() {
            return amount;
        }

        public void setAmount(Float amount) {
            this.amount = amount;
        }
    }

    private static class AccountCreditResponse {
        private String message;

        @JsonCreator
        public AccountCreditResponse(String message) {
            this.message = message;
        }

        public AccountCreditResponse() {
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
