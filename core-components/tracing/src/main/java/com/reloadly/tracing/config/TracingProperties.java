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

import io.jaegertracing.Configuration;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(TracingProperties.PREFIX)
public class TracingProperties {

    public static final String PREFIX = "reloadly.tracing";

    private static final Configuration.Propagation[] DEFAULT_PROPAGATION_FORMATS =
            {Configuration.Propagation.B3, Configuration.Propagation.JAEGER};
    private static final String DEFAULT_JAEGER_ENDPOINT = "http://localhost:14268/api/traces";

    private Configuration.Propagation[] jaegerPropagationFormats = DEFAULT_PROPAGATION_FORMATS;
    /**
     * Instructs the Reporter to log finished span IDs. The reporter may need to be given a Logger for this option to take effect.
     */
    private boolean jaegerReporterLogSpans = true;
    /**
     * Defines the max size of the in-memory buffer used to keep spans before they are sent out.
     */
    private int jaegerReporterMaxQueueSize = 100;
    /**
     * Defines how frequently the report flushes span batches. Reporter can also flush the batch if the batch size reaches the maximum UDP packet size (~64Kb).
     */
    private int jaegerReporterFlushInterval = 2000;

    /**
     * Defines the type of sampler to use, e.g. probabilistic, or const (see Sampling in Jaeger docs).
     */
    private JaegerSamplerType jaegerSamplerType = JaegerSamplerType.CONST;

    /**
     * Provides configuration value to the sampler, e.g. probability=0.001. Has different meanings for different samplers (see Sampling in Jaeger docs).
     */
    private Number jaegerSamplerParam = 1;

    /**
     * Enable or disable tracing.
     */
    private boolean enabled = true;

    /**
     * Whether to transmit spans to the remote agent. Disabling this will cause the spans to just logged.
     */
    private boolean transmitSpan = false;
    /**
     * The service name
     */
    private String serviceName = "reloadly-service";

    private String jaegerEndpoint = DEFAULT_JAEGER_ENDPOINT;
    /**
     * Jaeger agent host
     */
    private String jaegerAgentHost = "localhost";

    /**
     * Jaeger agent port
     */
    private int jaegerAgentPort = 14268;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getJaegerAgentHost() {
        return jaegerAgentHost;
    }

    public void setJaegerAgentHost(String jaegerAgentHost) {
        this.jaegerAgentHost = jaegerAgentHost;
    }

    public int getJaegerAgentPort() {
        return jaegerAgentPort;
    }

    public void setJaegerAgentPort(int jaegerAgentPort) {
        this.jaegerAgentPort = jaegerAgentPort;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }


    public String getJaegerEndpoint() {
        return jaegerEndpoint;
    }

    public void setJaegerEndpoint(String jaegerEndpoint) {
        this.jaegerEndpoint = jaegerEndpoint;
    }

    public boolean isTransmitSpan() {
        return transmitSpan;
    }

    public void setTransmitSpan(boolean transmitSpan) {
        this.transmitSpan = transmitSpan;
    }

    public JaegerSamplerType getJaegerSamplerType() {
        return jaegerSamplerType;
    }

    public void setJaegerSamplerType(JaegerSamplerType jaegerSamplerType) {
        this.jaegerSamplerType = jaegerSamplerType;
    }

    public Number getJaegerSamplerParam() {
        return jaegerSamplerParam;
    }

    public void setJaegerSamplerParam(Number jaegerSamplerParam) {
        this.jaegerSamplerParam = jaegerSamplerParam;
    }

    public boolean isJaegerReporterLogSpans() {
        return jaegerReporterLogSpans;
    }

    public void setJaegerReporterLogSpans(boolean jaegerReporterLogSpans) {
        this.jaegerReporterLogSpans = jaegerReporterLogSpans;
    }

    public int getJaegerReporterMaxQueueSize() {
        return jaegerReporterMaxQueueSize;
    }

    public void setJaegerReporterMaxQueueSize(int jaegerReporterMaxQueueSize) {
        this.jaegerReporterMaxQueueSize = jaegerReporterMaxQueueSize;
    }

    public int getJaegerReporterFlushInterval() {
        return jaegerReporterFlushInterval;
    }

    public void setJaegerReporterFlushInterval(int jaegerReporterFlushInterval) {
        this.jaegerReporterFlushInterval = jaegerReporterFlushInterval;
    }

    public Configuration.Propagation[] getJaegerPropagationFormats() {
        return jaegerPropagationFormats;
    }

    public void setJaegerPropagationFormats(Configuration.Propagation[] jaegerPropagationFormats) {
        this.jaegerPropagationFormats = jaegerPropagationFormats;
    }

    public enum JaegerSamplerType {
        CONST, PROBABILISTIC, RATELIMITING, REMOTE
    }
}
