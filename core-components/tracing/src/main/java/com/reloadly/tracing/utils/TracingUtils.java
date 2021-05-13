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
}
