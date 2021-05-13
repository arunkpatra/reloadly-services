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

import com.reloadly.tracing.aspects.TracedAspect;
import io.jaegertracing.internal.JaegerTracer;
import io.jaegertracing.internal.propagation.B3TextMapCodec;
import io.jaegertracing.internal.reporters.LoggingReporter;
import io.opentracing.Tracer;
import io.opentracing.propagation.Format;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Primary;
import org.springframework.util.StringUtils;

@Configuration(proxyBeanMethods = false)
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableConfigurationProperties(TracingProperties.class)
@ConditionalOnProperty(name = "reloadly.tracing.enabled", matchIfMissing = true)
public class TracingAutoConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(TracingAutoConfiguration.class);

    private final TracingProperties properties;

    public TracingAutoConfiguration(TracingProperties properties) {
        this.properties = properties;
    }

    @Bean
    public Tracer jaegerTracer() {
        B3TextMapCodec b3Codec = new B3TextMapCodec.Builder().build();
        io.jaegertracing.Configuration.CodecConfiguration codecConfig =
                new io.jaegertracing.Configuration.CodecConfiguration();
        for (io.jaegertracing.Configuration.Propagation p : properties.getJaegerPropagationFormats()) {
            codecConfig = codecConfig.withPropagation(p);
        }

        io.jaegertracing.Configuration.SamplerConfiguration samplerConfig =
                io.jaegertracing.Configuration.SamplerConfiguration.fromEnv()
                        .withType(properties.getJaegerSamplerType().name().toLowerCase())
                        .withParam(properties.getJaegerSamplerParam());

        io.jaegertracing.Configuration.SenderConfiguration senderConfiguration =
                io.jaegertracing.Configuration.SenderConfiguration.fromEnv()
                        .withAgentHost(properties.getJaegerAgentHost())
                        .withAgentPort(properties.getJaegerAgentPort());

        if (StringUtils.hasLength(properties.getJaegerEndpoint())) {
            senderConfiguration = senderConfiguration.withEndpoint(properties.getJaegerEndpoint());
        }

        io.jaegertracing.Configuration.ReporterConfiguration reporterConfig =
                io.jaegertracing.Configuration.ReporterConfiguration.fromEnv()
                        .withMaxQueueSize(properties.getJaegerReporterMaxQueueSize())
                        .withFlushInterval(properties.getJaegerReporterFlushInterval())
                        .withLogSpans(properties.isJaegerReporterLogSpans())
                        .withSender(senderConfiguration);

        io.jaegertracing.Configuration config =
                new io.jaegertracing.Configuration(properties.getServiceName())
                        .withCodec(codecConfig)
                        .withSampler(samplerConfig)
                        .withReporter(reporterConfig);
        JaegerTracer jaegerTracer;
        if (properties.isTransmitSpan()) {
            jaegerTracer = config.getTracerBuilder()
                    .registerInjector(Format.Builtin.HTTP_HEADERS, b3Codec)
                    .registerExtractor(Format.Builtin.HTTP_HEADERS, b3Codec).build();
        } else {
            // useful in tests
            LoggingReporter loggingReporter = new LoggingReporter(LOGGER);
            jaegerTracer = config.getTracerBuilder()
                    .withReporter(loggingReporter)
                    .registerInjector(Format.Builtin.HTTP_HEADERS, b3Codec)
                    .registerExtractor(Format.Builtin.HTTP_HEADERS, b3Codec).build();
        }

        return jaegerTracer;
    }

    @Bean
    @Primary
    @ConditionalOnBean({Tracer.class})
    public ReloadlyTracer reloadlyTracer(Tracer tracer) {
        return new ReloadlyTracer(properties.isEnabled(), tracer);
    }

    @Bean("tracedAspect")
    public TracedAspect tracedAspect(ReloadlyTracer tracer) {
        LOGGER.info("Instantiating TracedAspect bean.");
        return new TracedAspect(tracer);
    }
}
