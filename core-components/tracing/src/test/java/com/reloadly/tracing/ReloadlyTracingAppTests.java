package com.reloadly.tracing;

import com.reloadly.tracing.aspects.TracedAspect;
import com.reloadly.tracing.config.ReloadlyTracer;
import com.reloadly.tracing.service.ParentService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class ReloadlyTracingAppTests extends AbstractIntegrationTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReloadlyTracingAppTests.class);

    @Autowired
    private ApplicationContext context;

    @Autowired
    private ParentService parentService;

    @Test
    void context_should_load() {
        validateBeanExistence(ReloadlyTracer.class, TracedAspect.class);
    }

    @Test
    public void testLocalNestedSpansTest() {

        LOGGER.info("Starting testLocalNestedSpansTest.");
        String tracedResult = parentService.someParentMethod();
        assertThat(tracedResult).isEqualTo("Hello World from Parent -> Hello World from Child");
        LOGGER.info("Result was traced");

    }

    private void validateBeanExistence(Class<?>... types) {
        Arrays.stream(types).forEach(t -> {
            assertThat(context.getBeanNamesForType(t).length).isNotEqualTo(0);
        });
    }
}
