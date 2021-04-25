package com.reloadly.autoconfig.notification.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(NotificationClientProperties.PREFIX)
public class NotificationClientProperties {

    public static final String PREFIX = "reloadly.notification";

    /**
     * (Optional) Is Reloadly Notification integration enabled?
     */
    private boolean enabled;

    /**
     * Supress notifications, e.g. in Tests.
     */
    private boolean suppress;

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
}
