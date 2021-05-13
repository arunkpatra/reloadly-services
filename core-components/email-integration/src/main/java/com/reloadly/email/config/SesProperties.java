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

package com.reloadly.email.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Email auto-configuration properties.
 *
 * @author Arun Patra
 */
@ConfigurationProperties(SesProperties.PREFIX)
public class SesProperties {

    public static final String PREFIX = "reloadly.integration.email.ses";

    private static final String DEFAULT_FROM_EMAIL_ID = "info@reloadly.com";
    private static final String DEFAULT_AWS_REGION = "ap-south-1";

    /**
     * (Optional) Is AWS Simple Email Service integration enabled?
     */
    private boolean enabled;

    /**
     * The AWS region
     */
    private String region = DEFAULT_AWS_REGION;

    /**
     * Does not send actual Email if set.
     */
    private boolean dryRun = true;

    /**
     * The From email ID
     */
    private String fromEmailId = DEFAULT_FROM_EMAIL_ID;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isDryRun() {
        return dryRun;
    }

    public void setDryRun(boolean dryRun) {
        this.dryRun = dryRun;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getFromEmailId() {
        return fromEmailId;
    }

    public void setFromEmailId(String fromEmailId) {
        this.fromEmailId = fromEmailId;
    }
}
