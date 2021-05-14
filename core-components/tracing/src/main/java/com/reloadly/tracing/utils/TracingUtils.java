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

package com.reloadly.tracing.utils;

import com.reloadly.tracing.config.ReloadlyTracer;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.MessageHeaders;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TracingUtils {

    public static HttpHeaders getPropagatedHttpHeaders(HttpHeaders httpHeaders, ApplicationContext context) {
        try {
            ReloadlyTracer reloadlyTracer = context.getBean(ReloadlyTracer.class);
            reloadlyTracer.extractMetadataFromExistingSpan().forEach(httpHeaders::add);
        } catch (BeansException e) {
            // NOOP
        }
        return httpHeaders;
    }

    public static MessageHeaders getPropagatedMessageHeaders(ApplicationContext context) {
        try {
            Map<String, Object> headers = new HashMap<>();
            ReloadlyTracer reloadlyTracer = context.getBean(ReloadlyTracer.class);
            reloadlyTracer.extractMetadataFromExistingSpan().forEach(headers::put);
            return new MessageHeaders(headers);
        } catch (BeansException e) {
            // NOOP
        }
        return new MessageHeaders(Collections.emptyMap());
    }

    public static void addTraceTag(String key, String value, ApplicationContext context) {
        try {
            ReloadlyTracer reloadlyTracer = context.getBean(ReloadlyTracer.class);
            reloadlyTracer.tracer.activeSpan().setTag(key, value);
        } catch (Exception e) {
            // NOOP
        }
    }
}
