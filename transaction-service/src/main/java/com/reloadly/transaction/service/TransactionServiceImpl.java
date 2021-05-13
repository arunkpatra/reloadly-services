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

package com.reloadly.transaction.service;

import com.reloadly.transaction.config.TransactionServiceProperties;
import com.reloadly.transaction.entity.TransactionEntity;
import com.reloadly.transaction.exception.KafkaProcessingException;
import com.reloadly.transaction.exception.ReloadlyTxnSvcException;
import com.reloadly.transaction.model.TransactionRequest;
import com.reloadly.transaction.model.TransactionResponse;
import com.reloadly.transaction.repository.AirtimeSendTxnRepository;
import com.reloadly.transaction.repository.MoneyReloadTxnRepository;
import com.reloadly.transaction.repository.TransactionRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * This exposes capabilities related to user transaction requests.
 *
 * @author Arun Patra
 */
@Service
public class TransactionServiceImpl extends TransactionProcessingSupport implements TransactionService {

    public TransactionServiceImpl(TransactionRepository transactionRepository,
                                  MoneyReloadTxnRepository moneyReloadTxnRepository,
                                  AirtimeSendTxnRepository airtimeSendTxnRepository,
                                  TransactionServiceProperties properties,
                                  KafkaTemplate<String, String> template,
                                  ApplicationContext context) {
        super(transactionRepository, moneyReloadTxnRepository, airtimeSendTxnRepository, properties, template, context);
    }

    /**
     * Accepts or rejects a transaction initiated by the caller. Note that, the transaction response is
     * returned immediately. However, the transaction is picked up by the Transaction Processor component and
     * processed as needed. Their is no guarantee that a transaction eventually succeeds, but whatever the result,
     * the caller is necessarily notified.
     * <p>
     * The caller can always query back the status of a transaction.
     *
     * @param request The transaction request object.
     * @return A transaction response object.
     * @throws ReloadlyTxnSvcException If the transaction could not be handled.
     * @author Arun Patra
     */
    @Override
    @Transactional
    public TransactionResponse acceptTransaction(TransactionRequest request) throws ReloadlyTxnSvcException {

        Assert.notNull(request, "request can not be null");
        Assert.notNull(request.getTransactionType(), "Transaction type can not be null");
        String uid = getUid();
        Assert.notNull(uid, "UID can not be null");

        TransactionEntity te = addTransactionRecord(uid, request);
        switch (request.getTransactionType()) {
            case ADD_MONEY:
                addMoneyReloadTxnRecord(te, request);
                break;
            case SEND_AIRTIME:
                addSendAirtimeTxnRecord(te, request);
                break;
            default:
                throw new ReloadlyTxnSvcException("Unknown transaction type");
        }

        return new TransactionResponse(te.getTxnId(), te.getTransactionStatus());
    }

    /**
     * Post a transaction to Kafka.
     *
     * @param txnId The transaction ID.
     * @throws KafkaProcessingException If an error occurs.
     */
    @Override
    public void postTransactionToKafkaTopic(String txnId) throws KafkaProcessingException {
        postTransactionToKafka(txnId);
    }
}
