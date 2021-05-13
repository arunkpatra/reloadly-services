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

package com.reloadly.tracing.annotation;

import java.lang.annotation.*;

/**
 * A method annotated with this annotation will be traced if tracing is enabled.
 *
 * @author Arun Patra
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Traced {

    /**
     * The Operation name.
     *
     * @return operation name.
     */
    String operationName();

    /**
     * The type of headers to be extracted. Applicable only when a propagated span context
     * is to be utilized.
     *
     * @return The header type.
     */
    HeaderType headerType() default HeaderType.ALL;

    /**
     * Indicates how a span is to be created. See {@link SpanType}. The default is {@link SpanType#PROPAGATED}.
     *
     * @return The span type to be created.
     */
    SpanType spanType() default SpanType.PROPAGATED;

    /**
     * The trace tags to be added.
     *
     * @return The tags to be added.
     */
    TraceTag[] traceTags() default {};

    enum HeaderType {
        /**
         * HTTP headers are to be propagated.
         */
        HTTP,

        /**
         * gRPC headers are to be propagated.
         */
        GRPC,

        /**
         * Kafka headers are to be propagated.
         */
        KAFKA,

        /**
         * All header types will be tried to get context information.
         */
        ALL
    }

    enum SpanType {
        /**
         * Start a new span
         */
        NEW,

        /**
         * Create span from a locally(in same process) existing parent span.
         */
        EXISTING,

        /**
         * Create span from a propagated(from another process) parent span.
         */
        PROPAGATED
    }
}
