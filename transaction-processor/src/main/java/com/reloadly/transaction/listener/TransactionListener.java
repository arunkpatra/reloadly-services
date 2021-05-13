package com.reloadly.transaction.listener;

import org.springframework.messaging.MessageHeaders;

/**
 * Listener to handle incoming transaction messages on a Kafka topic.
 *
 * @author Arun Patra
 */
public interface TransactionListener {

    /**
     * Listens for inbound messages in a kafka topic.
     *
     * @param txnId          The transaction ID to be processed.
     * @param messageHeaders The message headers
     */
    void listen(String txnId, MessageHeaders messageHeaders);
}
