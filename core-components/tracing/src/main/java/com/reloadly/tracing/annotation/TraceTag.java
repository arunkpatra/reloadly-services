package com.reloadly.tracing.annotation;

import java.lang.annotation.*;

/**
 * A carrier for tracing tags.
 *
 * @author Arun Patra
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface TraceTag {

    /**
     * The key name
     *
     * @return Key name.
     */
    String key();

    /**
     * The value
     *
     * @return value
     */
    String value();
}
