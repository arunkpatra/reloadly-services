package com.reloadly.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Authentication Service auto-configuration properties.
 *
 * @author Arun Patra
 */
@ConfigurationProperties(ReloadlyAuthProperties.PREFIX)
public class ReloadlyAuthProperties {

    public static final String PREFIX = "reloadly.auth";

    /**
     * (Optional) Is Reloadly auth integration enabled?
     */
    private boolean enabled;

    /**
     * Reloadly auth service endpoint..
     */
    private String reloadlyAuthServiceEndpoint;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getReloadlyAuthServiceEndpoint() {
        return reloadlyAuthServiceEndpoint;
    }

    public void setReloadlyAuthServiceEndpoint(String reloadlyAuthServiceEndpoint) {
        this.reloadlyAuthServiceEndpoint = reloadlyAuthServiceEndpoint;
    }
}
