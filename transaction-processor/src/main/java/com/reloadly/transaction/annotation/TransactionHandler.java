package com.reloadly.transaction.annotation;

import com.reloadly.transaction.model.TransactionType;
import com.reloadly.transaction.processor.TransactionProcessor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * All concrete {@link TransactionProcessor} implementations, need to be annotated by this annotation to be picked up
 * by the transaction manager.
 *
 * @author Arun Patra
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface TransactionHandler {
    /**
     * The {@link TransactionType} that the transaction processor supports.
     *
     * @return The transaction type.
     */
    String value();

    /**
     * Description of the Handler
     *
     * @return The description of the transaction processor.
     */
    String description() default "";
}
