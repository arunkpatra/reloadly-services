package com.reloadly.tracing.aspects;

import com.reloadly.tracing.annotation.Traced;
import com.reloadly.tracing.config.ReloadlyTracer;
import io.opentracing.Span;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Aspect to proxy Traced methods.
 *
 * @author Arun Patra
 */
@Aspect
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
        return invokeUnderlyingOperationInScope(traced, null, joinPoint);
    }

    private Object handleWithLocalParentSpan(Traced traced, ProceedingJoinPoint joinPoint) throws Throwable {
        return invokeUnderlyingOperationInScope(traced, null, joinPoint);
    }

    private Object handleNewSpan(Traced traced, ProceedingJoinPoint joinPoint) throws Throwable {
        return invokeUnderlyingOperationInScope(traced, null, joinPoint);
    }

    private Object invokeUnderlyingOperationInScope(Traced traced, Span currentSpan, ProceedingJoinPoint joinPoint) throws Throwable {
        Object proceed;
        // Do real stuff
        LOGGER.trace("[Trace: #{}] Invoking underlying operation", traced.operationName());
        proceed = joinPoint.proceed();
        return proceed;
    }
}
