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

package com.reloadly.transaction.processor.moneyreload;

import com.reloadly.autoconfig.notification.service.ReloadlyNotification;
import com.reloadly.commons.model.account.AccountCreditReq;
import com.reloadly.commons.model.account.AccountCreditResp;
import com.reloadly.commons.model.account.AccountInfo;
import com.reloadly.tracing.utils.TracingUtils;
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
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

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
                                           RestTemplate restTemplate,
                                           ReloadlyNotification notification,
                                           ApplicationContext context) {
        super(transactionProcessorProperties, restTemplate, transactionRepository, notification, context);
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
            markTransactionAsSuccessful(txnEntity);
            LOGGER.info("Money reload successful.");
            sendNotifications(txnEntity.getUid(), true);
        } catch (ReloadlyTxnProcessingException e) {
            markTransactionAsFailed(txnEntity);
            LOGGER.error("Money reload transaction with txnId: {} has failed with reason {}", txnEntity.getTxnId(), e.getMessage());
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
        MoneyReloadTxnEntity mrte = moneyReloadTxnRepository.getByTxnId(txnEntity.getTxnId())
                .orElseThrow(() -> new ReloadlyTxnProcessingException(
                        String.format("Money reload transaction with ID: %s was not found", txnEntity.getTxnId())));

        Float amount = mrte.getAmount();
        // Now make call

        Assert.hasLength(uid, "UID can not be empty");
        Assert.notNull(amount, "Amount can not be null");

        // Exchange
        try {
            ResponseEntity<AccountCreditResp> response =
                    restTemplate.postForEntity(properties.getReloadlyAccountServiceEndpoint()
                                    .concat("/account/credit/".concat(uid)),
                            new HttpEntity<>(new AccountCreditReq(amount),
                                    TracingUtils.getPropagatedHttpHeaders(getHeaders(), context)),
                            AccountCreditResp.class);
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
}
