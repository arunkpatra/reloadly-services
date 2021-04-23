package com.reloadly.security.mock.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(MockSecurityProperties.PREFIX)
public class MockSecurityProperties {

    public static final String PREFIX = "reloadly.auth.mock";

    /**
     * (Optional) Is Firebase integration enabled?
     */
    private boolean enabled;

    /**
     * The roles to inject
     */
    private List<String> roles = new ArrayList<>();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
