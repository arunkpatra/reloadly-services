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

package com.reloadly.transaction.listener;

import com.reloadly.transaction.service.TransactionProcessorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;

/**
 * Listener to handle incoming transaction messages on a Kafka topic.
 *
 * @author Arun Patra
 */
@Service
public class TransactionListenerImpl implements TransactionListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionListenerImpl.class);
    private final TransactionProcessorService transactionProcessorService;

    public TransactionListenerImpl(TransactionProcessorService transactionProcessorService) {
        this.transactionProcessorService = transactionProcessorService;
    }

    /**
     * Listens for inbound messages in a kafka topic.
     *
     * @param txnId          The transaction ID to be processed.
     * @param messageHeaders The message headers
     */
    @Override
    @KafkaListener(topics = "com.reloadly.inbound.txn.topic", groupId = "inboundTxnProcessingConsumerGrp")
    public void listen(String txnId, MessageHeaders messageHeaders) {
        LOGGER.info("Message received. Txn ID: {}", txnId);
        try {
            transactionProcessorService.processInboundTransaction(txnId, messageHeaders);
        } catch (Throwable t) {
            LOGGER.error("Failed to process message. Root cause: {}", t.getMessage());
        }
    }
}
