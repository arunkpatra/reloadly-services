package com.reloadly.transaction.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface TransactionHandler {
    /**
     * The transactionType
     *
     * @return The name of the function.
     */
    String value();

    /**
     * Description of the Handler
     *
     * @return
     */
    String description() default "";
}
