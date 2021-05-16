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

package com.reloadly.tracing.service;

import com.reloadly.tracing.annotation.TraceTag;
import com.reloadly.tracing.annotation.Traced;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.MessageHeaders;

public class ParentServiceImpl implements ParentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParentServiceImpl.class);
    private final ChildService childService;

    public ParentServiceImpl(ChildService childService) {
        this.childService = childService;
    }

    @Override
    @Traced(operationName = "some-parent-method",
            traceTags = @TraceTag(key = "a", value = "b"),
            headerType = Traced.HeaderType.HTTP)
    public String someParentMethod() {
        String parentMessage = "Hello World from Parent";
        String childMessage = childService.someChildMethod();
        LOGGER.info("Returning: {} -> {}", parentMessage, childMessage);
        return String.format("%s -> %s", parentMessage, childMessage);
    }

    @Override
    @Traced(operationName = "some-parent-method-with-message-headers", spanType = Traced.SpanType.PROPAGATED,
            headerType = Traced.HeaderType.KAFKA)
    public String someParentMethodWithMessageHeaders(MessageHeaders headers) {
        String parentMessage = "Hello World from Parent";
        String childMessage = childService.someChildMethod();
        LOGGER.info("Returning: {} -> {}", parentMessage, childMessage);
        return String.format("%s -> %s", parentMessage, childMessage);
    }
}
