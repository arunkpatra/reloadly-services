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

package com.reloadly.tracing.config;

import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.propagation.Format;
import io.opentracing.propagation.TextMap;
import io.opentracing.propagation.TextMapAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Wraps a {@link Tracer} implementation. Currently we use a Jaeger implementation
 * of the tracer.
 */
public class ReloadlyTracer {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReloadlyTracer.class);
    public final boolean enabled;
    public final Tracer tracer;

    public ReloadlyTracer(boolean enabled, Tracer tracer) {
        this.enabled = enabled;
        this.tracer = tracer;
    }

    public Map<String, String> extractMetadataFromExistingSpan() {
        Map<String, String> metadataMap = new HashMap<>();
        if (enabled) {
            try {
                Span ss = tracer.activeSpan();
                TextMap metadataCarrier = new TextMapAdapter(new HashMap<>());
                tracer.inject(ss.context(), Format.Builtin.HTTP_HEADERS, metadataCarrier);
                LOGGER.trace("Injected carrier data successfully.");
                for (Map.Entry<String, String> e : metadataCarrier) {
                    metadataMap.put(e.getKey(), e.getValue());
                }
            } catch (Throwable t) {
                LOGGER.trace("Failed to inject carrier data. Hence, headers will not be propagated. Error: {}", t.getMessage());
            }
        }
        return metadataMap;
    }
}
