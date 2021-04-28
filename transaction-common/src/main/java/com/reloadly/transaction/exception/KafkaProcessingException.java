package com.reloadly.transaction.exception;

import com.reloadly.commons.exceptions.ReloadlyException;
/**
 * Exception indicating Kafka processing exception.
 *
 * @author Arun Patra
 */
public class KafkaProcessingException extends ReloadlyException {

    private static final String DEFAULT_MESSAGE = "Could not process transaction";

    public KafkaProcessingException() {
        super(DEFAULT_MESSAGE);
    }

    public KafkaProcessingException(String message) {
        super(message);
    }

    public KafkaProcessingException(Exception e) {
        super(DEFAULT_MESSAGE, e);
    }

    public KafkaProcessingException(String message, Exception e) {
        super(message, e);
    }

}
