package com.reloadly.transaction.processor.airtime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.reloadly.autoconfig.notification.service.ReloadlyNotification;
import com.reloadly.transaction.annotation.TransactionHandler;
import com.reloadly.transaction.config.TransactionProcessorProperties;
import com.reloadly.transaction.entity.AirtimeSendTxnEntity;
import com.reloadly.transaction.entity.TransactionEntity;
import com.reloadly.transaction.exception.ReloadlyTxnProcessingException;
import com.reloadly.transaction.processor.AbstractTransactionProcessor;
import com.reloadly.transaction.repository.AirtimeSendTxnRepository;
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
 * An illustrative implementation of a Airtime Send transaction processor.
 *
 * @author Arun Patra
 */
@TransactionHandler(value = "SEND_AIRTIME", description = "Transaction processor to hand sending airtime")
public class AirTimeSendTransactionProcessor extends AbstractTransactionProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(AirTimeSendTransactionProcessor.class);
    private final AirtimeSendTxnRepository airtimeSendTxnRepository;

    public AirTimeSendTransactionProcessor(TransactionProcessorProperties transactionProcessorProperties,
                                           TransactionRepository transactionRepository,
                                           AirtimeSendTxnRepository airtimeSendTxnRepository,
                                           RestTemplate restTemplate, ReloadlyNotification notification) {
        super(transactionProcessorProperties, restTemplate, transactionRepository, notification);
        this.airtimeSendTxnRepository = airtimeSendTxnRepository;
    }

    @Override
    public void processTransaction(TransactionEntity txnEntity) throws ReloadlyTxnProcessingException {

        LOGGER.info("Handling airtime send transaction.");
        markTransactionAsProcessing(txnEntity);

        // >>>
        // >>> Your business logic goes here. Do actual airtime send operation
        // >>>

        // Now debit account. Account can only be debited by the Account Microservice.
        try {
            debitAccount(txnEntity);
        } catch (ReloadlyTxnProcessingException e) {
            markTransactionAsFailed(txnEntity);
            LOGGER.error("Airtime send transaction with txnId: {} has failed with reason {}", txnEntity.getTxnId(), e.getMessage());
            sendNotifications(txnEntity.getUid(), false);
        } catch (Exception e) {
            throw new ReloadlyTxnProcessingException("Unhandled exception occurred. Rolling back transaction. Root cause: " + e.getMessage(), e);
        }

        markTransactionAsSuccessful(txnEntity);
        LOGGER.info("Airtime send successful.");
        sendNotifications(txnEntity.getUid(), true);
    }

    /**
     * Send notifications
     *
     * @param uid    The UID
     * @param status If the transaction has failed.
     */
    private void sendNotifications(String uid, boolean status) {
        String emailSub = status ? "Airtime Send Successful" : "Airtime Send Failed";
        String message = status ? "Airtime has been sent successfully. Thank you." :
                "Your airtime send transaction has failed.";
        try {
            AccountInfo accountInfo = getAccountInfo(uid);
            sendEmailMessage(accountInfo, emailSub, message);
            sendSMSMessage(accountInfo, message);
        } catch (Exception e) {
            // Log and ignore
            LOGGER.error("Error while sending notification. Root cause: {}", e.getMessage());
        }
    }

    private void debitAccount(TransactionEntity txnEntity) throws ReloadlyTxnProcessingException {
        String uid = txnEntity.getUid();
        Optional<AirtimeSendTxnEntity> asTxnOpt = airtimeSendTxnRepository.getByTxnId(txnEntity.getTxnId());
        if (!asTxnOpt.isPresent()) {
            throw new ReloadlyTxnProcessingException(String.format("Airtime send transaction with ID: %s was not found", txnEntity.getTxnId()));
        }
        Float amount = asTxnOpt.get().getAmount();
        String phoneNumber = asTxnOpt.get().getPhoneNumber();

        // Now make call

        Assert.hasLength(uid, "UID can not be empty");
        Assert.notNull(amount, "Amount can not be null");
        Assert.notNull(phoneNumber, "Phone number can not be null");

        // Exchange - Debit call to Account microservice
        try {
            ResponseEntity<AccountDebitResponse> response =
                    restTemplate.postForEntity(properties.getReloadlyAccountServiceEndpoint().concat("/account/debit/".concat(uid)),
                            new HttpEntity<>(new AccountDebitRequest(amount), getHeaders()),
                            AccountDebitResponse.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                LOGGER.info("Debit transaction successful");
            } else {
                throw new ReloadlyTxnProcessingException("Debit transaction failed. Status code: "
                        .concat(response.getStatusCode().toString()));
            }
        } catch (RestClientException e) {
            throw new ReloadlyTxnProcessingException("An error occurred. Root cause: " + e.getMessage());
        }
    }

    private static class AccountDebitRequest {
        private Float amount;

        @JsonCreator
        public AccountDebitRequest(Float amount) {
            this.amount = amount;
        }

        public AccountDebitRequest() {
        }

        public Float getAmount() {
            return amount;
        }

        public void setAmount(Float amount) {
            this.amount = amount;
        }
    }

    private static class AccountDebitResponse {
        private Boolean successful;
        private String message;

        @JsonCreator
        public AccountDebitResponse(Boolean successful, String message) {
            this.successful = successful;
            this.message = message;
        }

        public AccountDebitResponse() {
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Boolean getSuccessful() {
            return successful;
        }

        public void setSuccessful(Boolean successful) {
            this.successful = successful;
        }
    }
}
