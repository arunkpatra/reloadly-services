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

package com.reloadly.autoconfig.notification.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Notification integration auto-configuration properties.
 *
 * @author Arun Patra
 */
@ConfigurationProperties(NotificationClientProperties.PREFIX)
public class NotificationClientProperties {

    public static final String PREFIX = "reloadly.notification";
    private static final String DEFAULT_NOTIFICATION_SVC_ENDPOINT = "http://localhost:8082";
    /**
     * (Optional) Is Reloadly Notification integration enabled?
     */
    private boolean enabled;

    /**
     * Supress notifications, e.g. in Tests.
     */
    private boolean suppress;

    /**
     * Reloadly notification service endpoint.
     */
    private String reloadlyNotificationServiceEndpoint = DEFAULT_NOTIFICATION_SVC_ENDPOINT;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isSuppress() {
        return suppress;
    }

    public void setSuppress(boolean suppress) {
        this.suppress = suppress;
    }

    public String getReloadlyNotificationServiceEndpoint() {
        return reloadlyNotificationServiceEndpoint;
    }

    public void setReloadlyNotificationServiceEndpoint(String reloadlyNotificationServiceEndpoint) {
        this.reloadlyNotificationServiceEndpoint = reloadlyNotificationServiceEndpoint;
    }
}
