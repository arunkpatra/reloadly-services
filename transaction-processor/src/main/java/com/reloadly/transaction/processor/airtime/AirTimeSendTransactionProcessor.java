package com.reloadly.transaction.processor.airtime;

import com.reloadly.autoconfig.notification.service.ReloadlyNotification;
import com.reloadly.commons.model.account.AccountDebitReq;
import com.reloadly.commons.model.account.AccountDebitResp;
import com.reloadly.commons.model.account.AccountInfo;
import com.reloadly.tracing.utils.TracingUtils;
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
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

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
                                           RestTemplate restTemplate,
                                           ReloadlyNotification notification,
                                           ApplicationContext context) {
        super(transactionProcessorProperties, restTemplate, transactionRepository, notification, context);
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
            markTransactionAsSuccessful(txnEntity);
            LOGGER.info("Airtime send successful.");
            sendNotifications(txnEntity.getUid(), true);
        } catch (ReloadlyTxnProcessingException e) {
            markTransactionAsFailed(txnEntity);
            LOGGER.error("Airtime send transaction with txnId: {} has failed with reason {}", txnEntity.getTxnId(), e.getMessage());
            sendNotifications(txnEntity.getUid(), false);
        }
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

        AirtimeSendTxnEntity aste = airtimeSendTxnRepository.getByTxnId(txnEntity.getTxnId())
                .orElseThrow(() -> new ReloadlyTxnProcessingException(
                        String.format("Airtime send transaction with ID: %s was not found", txnEntity.getTxnId())));

        Float amount = aste.getAmount();
        String phoneNumber = aste.getPhoneNumber();

        // Now make call

        Assert.hasLength(uid, "UID can not be empty");
        Assert.notNull(amount, "Amount can not be null");
        Assert.notNull(phoneNumber, "Phone number can not be null");

        // Exchange - Debit call to Account microservice
        try {
            ResponseEntity<AccountDebitResp> response =
                    restTemplate.postForEntity(properties.getReloadlyAccountServiceEndpoint()
                                    .concat("/account/debit/".concat(uid)),
                            new HttpEntity<>(new AccountDebitReq(amount),
                                    TracingUtils.getPropagatedHttpHeaders(getHeaders(), context)),
                            AccountDebitResp.class);
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
}
