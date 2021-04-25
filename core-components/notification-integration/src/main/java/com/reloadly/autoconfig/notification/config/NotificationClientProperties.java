package com.reloadly.autoconfig.notification.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

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
