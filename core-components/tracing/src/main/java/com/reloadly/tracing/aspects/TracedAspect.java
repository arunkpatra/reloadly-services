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

package com.reloadly.tracing.aspects;

import com.reloadly.tracing.annotation.TraceTag;
import com.reloadly.tracing.annotation.Traced;
import com.reloadly.tracing.config.ReloadlyTracer;
import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.SpanContext;
import io.opentracing.propagation.Format;
import io.opentracing.propagation.TextMap;
import io.opentracing.propagation.TextMapAdapter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessageHeaders;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Aspect to proxy Traced methods.
 *
 * @author Arun Patra
 */
@Aspect
@Order
public class TracedAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(TracedAspect.class);
    private final ReloadlyTracer tracer;

    public TracedAspect(ReloadlyTracer tracer) {
        LOGGER.trace("Creating TracedAspect aspect.");
        this.tracer = tracer;
    }

    @Pointcut("@annotation(com.reloadly.tracing.annotation.Traced)")
    public void tracedTarget() {
    }

    @Around(value = "tracedTarget()")
    public Object submit(ProceedingJoinPoint joinPoint) throws Throwable {

        Traced traced = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(Traced.class);
        LOGGER.trace("[Trace: #{}] Starting trace.", traced.operationName());
        switch (traced.spanType()) {
            case PROPAGATED:
                return handleWithPropagatedParentSpan(traced, joinPoint);
            case EXISTING:
                return handleWithLocalParentSpan(traced, joinPoint);
            default:
                // New span will be created
                return handleNewSpan(traced, joinPoint);
        }
    }

    private Object handleWithPropagatedParentSpan(Traced traced, ProceedingJoinPoint joinPoint) throws Throwable {
        LOGGER.trace("[Trace: #{}] Will try to use existing span as parent span.", traced.operationName());
        SpanContext propagatedContext = tracer.tracer.extract(Format.Builtin.HTTP_HEADERS,
                getCarrier(traced, joinPoint));
        if (null != propagatedContext) {
            LOGGER.trace("[Trace: #{}] Extracted propagated context -> spanId={}, traceId={}", traced.operationName(),
                    propagatedContext.toSpanId(), propagatedContext.toTraceId());
            Span currentSpan =
                    enhanceSpanFromRequest(traced, tracer.tracer.buildSpan(traced.operationName()).asChildOf(propagatedContext).start(),
                            joinPoint);
            LOGGER.trace("[Trace: #{}] Started child span with propagated parent context. Parent spanId={}, " +
                            "Parent traceId={}, Child spanId={}. Child traceId={}",
                    traced.operationName(),
                    propagatedContext.toSpanId(),
                    propagatedContext.toTraceId(),
                    currentSpan.context().toSpanId(),
                    currentSpan.context().toTraceId()
            );
            return invokeUnderlyingOperationInScope(traced, currentSpan, joinPoint);
        } else {
            LOGGER.trace("[Trace: #{}] Failed to extract propagated context. Will try with local parent span.", traced.operationName());
            return handleWithLocalParentSpan(traced, joinPoint);
        }
    }

    private Object handleWithLocalParentSpan(Traced traced, ProceedingJoinPoint joinPoint) throws Throwable {
        LOGGER.trace("[Trace: #{}] Will try to use existing span as parent span.", traced.operationName());
        String operationName = traced.operationName();
        Span ps = tracer.tracer.activeSpan();
        if (ps != null) {
            SpanContext parentSpanContext = ps.context();
            Span currentSpan =
                    enhanceSpanFromRequest(traced, tracer.tracer.buildSpan(operationName).asChildOf(parentSpanContext).start(),
                            joinPoint);
            LOGGER.trace("[Trace: #{}] Started child span of existing propagated span. Parent spanId={}, " +
                            "Parent traceId={}, Child spanId={}. Child traceId={}",
                    traced.operationName(),
                    parentSpanContext.toSpanId(),
                    parentSpanContext.toTraceId(),
                    currentSpan.context().toSpanId(),
                    currentSpan.context().toTraceId());
            return invokeUnderlyingOperationInScope(traced, currentSpan, joinPoint);
        } else {
            LOGGER.trace("[Trace: #{}] No parent span exists. Will create new span", traced.operationName());
            return handleNewSpan(traced, joinPoint);
        }
    }

    private Object handleNewSpan(Traced traced, ProceedingJoinPoint joinPoint) throws Throwable {
        LOGGER.trace("[Trace: #{}] Will create new span.", traced.operationName());
        String operationName = traced.operationName();
        Span currentSpan = enhanceSpanFromRequest(traced, tracer.tracer.buildSpan(operationName).start(), joinPoint);
        LOGGER.trace("[Trace: #{}] Started new span -> spanId={}, traceId={}", traced.operationName(),
                currentSpan.context().toSpanId(), currentSpan.context().toTraceId());
        return invokeUnderlyingOperationInScope(traced, currentSpan, joinPoint);
    }

    private Object invokeUnderlyingOperationInScope(Traced traced, Span currentSpan, ProceedingJoinPoint joinPoint) throws Throwable {
        // Scope will be auto-closed
        try (Scope ignored = tracer.tracer.activateSpan(currentSpan)) {
            Object proceed;
            // Do real stuff
            LOGGER.trace("[Trace: #{}] Invoking underlying operation", traced.operationName());
            proceed = joinPoint.proceed();
            //currentSpan = enhanceSpanFromResponse2(currentSpan, proceed);
            // Finish span if one exists
            // The span is enhanced internally with exception data
            finishPan(traced, currentSpan, proceed);
            return proceed;
        } catch (Throwable t) {
            LOGGER.trace("[Trace: #{}] Immediate -> Error while trying to invoke underlying method: {}",
                    traced.operationName(), t.getMessage());
            if (null != t.getCause()) {
                LOGGER.trace("[Trace: #{}] Cause of underlying error: {}",
                        traced.operationName(), t.getCause().getMessage());
            }
            currentSpan = enhanceSpanFromException(traced, currentSpan, t);
            LOGGER.trace("[Trace: #{} Immediate -> Finishing span now.", traced.operationName());
            currentSpan.finish();
            LOGGER.trace("[Trace: #{}] Immediate -> Finished span with exception -> spanId={}, traceId={}",
                    traced.operationName(),
                    currentSpan.context().toSpanId(), currentSpan.context().toTraceId());
            throw t;
        }
    }

    private void finishPan(Traced traced, Span currentSpan, Object result) {

        currentSpan = enhanceSpanFromResponse(traced, currentSpan, result);
        currentSpan = currentSpan.setTag("service.call.status", "OK");
        currentSpan.finish();
        LOGGER.trace("[Trace: #{}] Finished span now -> spanId={}, traceId={}", traced.operationName(),
                currentSpan.context().toSpanId(), currentSpan.context().toTraceId());

    }

    private TextMap getCarrier(Traced traced, ProceedingJoinPoint joinPoint) {
        TextMap carrier = new TextMapAdapter(new HashMap<>());
        switch (traced.headerType()) {
            case ALL:
                TextMap gCarrier = getGrpcCarrier(traced, joinPoint);
                TextMap hCarrier = getHttpCarrier(traced, joinPoint);
                TextMap kCarrier = getKafkaCarrier(traced, joinPoint);
                for (Map.Entry<String, String> e : gCarrier) {
                    carrier.put(e.getKey(), e.getValue());
                }
                for (Map.Entry<String, String> e : hCarrier) {
                    carrier.put(e.getKey(), e.getValue());
                }
                for (Map.Entry<String, String> e : kCarrier) {
                    carrier.put(e.getKey(), e.getValue());
                }
                break;
            case GRPC:
                carrier = getGrpcCarrier(traced, joinPoint);
                break;
            case HTTP:
                carrier = getHttpCarrier(traced, joinPoint);
                break;
            case KAFKA:
                carrier = getKafkaCarrier(traced, joinPoint);
                break;
            default:
                break;
        }
        return carrier;
    }

    private Span enhanceSpanFromRequest(Traced traced, Span span, ProceedingJoinPoint joinPoint) {
        Map<String, String> tagMapHttp = getHttpRequestInfo(traced, joinPoint);
        for (Map.Entry<String, String> e : tagMapHttp.entrySet()) {
            span = span.setTag(e.getKey(), e.getValue());
        }
        Map<String, String> tagMapGrpc = getGrpcRequestInfo(traced, joinPoint);
        for (Map.Entry<String, String> e : tagMapGrpc.entrySet()) {
            span = span.setTag(e.getKey(), e.getValue());
        }

        Map<String, String> tagMapKafka = getKafkaRequestInfo(traced, joinPoint);
        for (Map.Entry<String, String> e : tagMapKafka.entrySet()) {
            span = span.setTag(e.getKey(), e.getValue());
        }

        for (TraceTag tt : traced.traceTags()) {
            span = span.setTag(tt.key(), tt.value());
        }
        return span;
    }

    private TextMap getGrpcCarrier(Traced traced, ProceedingJoinPoint joinPoint) {
        // TODO: Implement me
        return new TextMapAdapter(new HashMap<>());
    }

    private TextMap getHttpCarrier(Traced traced, ProceedingJoinPoint joinPoint) {
        TextMap httpMetadataCarrier = new TextMapAdapter(new HashMap<>());
        Object[] signatureArgs = joinPoint.getArgs();
        HttpHeaders httpHeaders = null;
        for (Object o : signatureArgs) {
            if (o instanceof HttpHeaders) {
                httpHeaders = (HttpHeaders) o;
            }
        }

        if (null != httpHeaders) {
            LOGGER.trace("[Trace: #{}] Extracted metadata -> {}", traced.operationName(), httpHeaders.toSingleValueMap());
            for (Map.Entry<String, String> e : httpHeaders.toSingleValueMap().entrySet()) {
                httpMetadataCarrier.put(e.getKey(), e.getValue());
            }
        }
        return httpMetadataCarrier;
    }

    private TextMap getKafkaCarrier(Traced traced, ProceedingJoinPoint joinPoint) {
        TextMap kafkaMetadataCarrier = new TextMapAdapter(new HashMap<>());
        Object[] signatureArgs = joinPoint.getArgs();
        MessageHeaders messageHeaders = null;
        for (Object o : signatureArgs) {
            if (o instanceof MessageHeaders) {
                messageHeaders = (MessageHeaders) o;
            }
        }

        if (null != messageHeaders) {
            LOGGER.trace("[Trace: #{}] Extracted metadata -> {}", traced.operationName(), messageHeaders);

            for (Map.Entry<String, Object> e : messageHeaders.entrySet()) {
                if (e.getValue() instanceof String) {
                    kafkaMetadataCarrier.put(e.getKey(), (String) e.getValue());
                }
            }
        }
        return kafkaMetadataCarrier;
    }

    private Map<String, String> getHttpRequestInfo(Traced traced, ProceedingJoinPoint joinPoint) {
        Map<String, String> tagMap = new HashMap<>();
        Object[] signatureArgs = joinPoint.getArgs();
        HttpServletRequest httpServletRequest = null;
        for (Object o : signatureArgs) {
            if (o instanceof HttpServletRequest) {
                httpServletRequest = (HttpServletRequest) o;
            }
        }
        if (null != httpServletRequest) {
            tagMap.put("http.uri", httpServletRequest.getRequestURI());
            tagMap.put("http.method", httpServletRequest.getMethod());
            tagMap.put("component", "net/http");
        }
        return tagMap;
    }

    private Map<String, String> getGrpcRequestInfo(Traced traced, ProceedingJoinPoint joinPoint) {
        // TODO: Implement me
        return new HashMap<>();
    }

    private Map<String, String> getKafkaRequestInfo(Traced traced, ProceedingJoinPoint joinPoint) {
        // TODO: Implement me
        return new HashMap<>();
    }

    private Span enhanceSpanFromResponse(Traced traced, Span currentSpan, Object proceed) {
        Span ss = currentSpan;
        if (proceed instanceof ResponseEntity) {
            ResponseEntity<?> responseEntity = (ResponseEntity<?>) proceed;
            ss = currentSpan.setTag("http.status_code", responseEntity.getStatusCode().value());
        }
        return ss;
    }

    private Span enhanceSpanFromException(Traced traced, Span currentSpan, Throwable t) {
        LOGGER.trace("[Trace: #{}] Extracting and adding tags from exception", traced.operationName());
        Span ss = currentSpan;
        ss = ss.setTag("service.call.status", "FAILED");
        LOGGER.trace("[Trace: #{}] Exception type is {}", traced.operationName(), t.getClass().getName());
        return ss;
    }
}
