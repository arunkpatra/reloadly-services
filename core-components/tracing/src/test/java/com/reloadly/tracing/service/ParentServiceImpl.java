package com.reloadly.tracing.service;

import com.reloadly.tracing.annotation.Traced;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParentServiceImpl implements ParentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParentServiceImpl.class);
    private final ChildService childService;

    public ParentServiceImpl(ChildService childService) {
        this.childService = childService;
    }

    @Override
    @Traced(operationName = "some-parent-method")
    public String someParentMethod() {
        String parentMessage = "Hello World from Parent";
        String childMessage = childService.someChildMethod();
        LOGGER.info("Returning: {} -> {}", parentMessage, childMessage);
        return String.format("%s -> %s", parentMessage, childMessage);
    }
}
