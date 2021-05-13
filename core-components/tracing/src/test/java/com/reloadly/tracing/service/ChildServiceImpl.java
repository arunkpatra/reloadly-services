package com.reloadly.tracing.service;


import com.reloadly.tracing.annotation.Traced;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChildServiceImpl implements ChildService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChildServiceImpl.class);

    @Override
    @Traced(operationName = "some-child-method", spanType = Traced.SpanType.EXISTING)
    public String someChildMethod() {
        String childMessage = "Hello World from Child";
        LOGGER.info("Returning: {}", childMessage);
        return childMessage;
    }
}
