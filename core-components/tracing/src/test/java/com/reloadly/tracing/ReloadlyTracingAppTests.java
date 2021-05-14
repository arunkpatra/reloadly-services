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

package com.reloadly.tracing;

import com.reloadly.tracing.aspects.TracedAspect;
import com.reloadly.tracing.config.ReloadlyTracer;
import com.reloadly.tracing.service.ParentService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.MessageHeaders;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
    public void test_local_nested_spans_test() {

        LOGGER.info("Starting test_local_nested_spans_test.");
        String tracedResult = parentService.someParentMethod();
        assertThat(tracedResult).isEqualTo("Hello World from Parent -> Hello World from Child");
        LOGGER.info("Result was traced");

    }

    @Test
    public void test_propagated_nested_spans_test() {
        Map<String, Object> map = new HashMap<>();
        map.put("kafka_offset", 1L);
        map.put("kafka_receivedTopic", "test.topic");
        map.put("kafka_groupId", "test.group_id");
        map.put("kafka_receivedTimestamp", (new Date()).getTime());
        LOGGER.info("Starting test_propagated_nested_spans_test.");
        String tracedResult = parentService.someParentMethodWithMessageHeaders(new MessageHeaders(map));
        assertThat(tracedResult)
                .isEqualTo("Hello World from Parent -> Hello World from Child", tracedResult);
        LOGGER.info("Result was traced");
    }

    private void validateBeanExistence(Class<?>... types) {
        Arrays.stream(types).forEach(t -> {
            assertThat(context.getBeanNamesForType(t).length).isNotEqualTo(0);
        });
    }
}
