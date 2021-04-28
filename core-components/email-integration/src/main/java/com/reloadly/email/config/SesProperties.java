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
