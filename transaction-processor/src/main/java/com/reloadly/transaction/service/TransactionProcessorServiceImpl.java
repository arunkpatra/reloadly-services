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

import com.reloadly.tracing.annotation.Traced;
import com.reloadly.transaction.entity.TransactionEntity;
import com.reloadly.transaction.exception.ReloadlyTxnProcessingException;
import com.reloadly.transaction.manager.TransactionManager;
import com.reloadly.transaction.model.TransactionStatus;
import com.reloadly.transaction.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Optional;

@Service
public class TransactionProcessorServiceImpl implements TransactionProcessorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionProcessorServiceImpl.class);
    private final TransactionRepository transactionRepository;
    private final TransactionManager transactionManager;

    protected TransactionProcessorServiceImpl(TransactionRepository transactionRepository,
                                              TransactionManager transactionManager) {
        this.transactionRepository = transactionRepository;
        this.transactionManager = transactionManager;
    }

    @Traced(operationName = "TransactionProcessor#processInboundTransaction", headerType = Traced.HeaderType.KAFKA)
    public void processInboundTransaction(String txnId, MessageHeaders messageHeaders) {
        Assert.notNull(txnId, "Transaction ID can not be null");

        //messageHeaders.forEach((key, value) -> LOGGER.info("Key: {}, Value: {}", key, value));

        Optional<TransactionEntity> txnOpt = transactionRepository.findByTxnId(txnId);
        if (!txnOpt.isPresent()) {
            LOGGER.error("Transaction ID: {} was not found.", txnId);
            return;
        }

        TransactionEntity txnEntity = txnOpt.get();

        // Reject if in one of the following states
        TransactionStatus txnStatus = txnEntity.getTransactionStatus();
        if (txnStatus.equals(TransactionStatus.PROCESSING) || txnStatus.equals(TransactionStatus.SUCCESSFUL)) {
            LOGGER.warn("Transaction ID: {} is has an incompatible state {}. Exiting.", txnId, txnStatus.name());
            return;
        }

        // Delegate to the transaction manager
        try {
            transactionManager.handleTransaction(txnEntity);
        } catch (ReloadlyTxnProcessingException e) {
            LOGGER.error("Error while processing transaction with txnID: {}. Root Cause: {}", txnId, e.getMessage());
        }
    }
}
