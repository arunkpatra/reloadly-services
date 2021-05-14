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

import com.reloadly.tracing.utils.TracingUtils;
import com.reloadly.transaction.config.TransactionServiceProperties;
import com.reloadly.transaction.entity.TransactionEntity;
import com.reloadly.transaction.exception.KafkaProcessingException;
import com.reloadly.transaction.model.TransactionRequest;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

/**
 * This provides Kafka support.
 *
 * @author Arun Patra
 */
public abstract class KafkaSupport {

    protected final TransactionServiceProperties properties;
    private final KafkaTemplate<String, String> template;
    protected final ApplicationContext context;

    public KafkaSupport(TransactionServiceProperties properties, KafkaTemplate<String, String> template,
                        ApplicationContext context) {
        this.properties = properties;
        this.template = template;
        this.context = context;
    }

    protected void postTransactionToKafka(String txnId) throws KafkaProcessingException {
        String topic = properties.getKafka().getInboundTransactionsTopic();
        try {
            Message<String> message = new Message<String>() {
                @Override
                public String getPayload() {
                    return txnId;
                }

                @Override
                public MessageHeaders getHeaders() {
                    return TracingUtils.getPropagatedMessageHeaders(context);
                }
            };
            template.setDefaultTopic(topic);
            template.send(message);
//            template.send(topic, te.getTxnId());
        } catch (Exception e) {
            throw new KafkaProcessingException("Failed to post transaction to Kafka. Root cause: ".concat(e.getMessage()), e);
        }
    }
}
