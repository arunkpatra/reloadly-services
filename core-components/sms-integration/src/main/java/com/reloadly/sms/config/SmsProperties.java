package com.reloadly.sms.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
/**
 * SMS integration auto-configuration properties.
 *
 * @author Arun Patra
 */
@ConfigurationProperties(SmsProperties.PREFIX)
public class SmsProperties {

    public static final String PREFIX = "reloadly.integration.sms.twilio";

    public static final String DEFAULT_COUNTRY_CODE = "+91";

    /**
     * (Optional) Is twilio integration enabled?
     */
    private boolean enabled = true;

    /**
     * Does not send actual SMS if set.
     */
    private boolean dryRun = true;

    /**
     * Twilio Messaging Service Id.
     */
    private String messagingServiceId;

    /**
     * Twilio account SID
     */
    private String twilioAccountSid;

    /**
     * Twilio Auth Token
     */
    private String twilioAuthToken;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getTwilioAccountSid() {
        return twilioAccountSid;
    }

    public void setTwilioAccountSid(String twilioAccountSid) {
        this.twilioAccountSid = twilioAccountSid;
    }

    public String getTwilioAuthToken() {
        return twilioAuthToken;
    }

    public void setTwilioAuthToken(String twilioAuthToken) {
        this.twilioAuthToken = twilioAuthToken;
    }

    public boolean isDryRun() {
        return dryRun;
    }

    public void setDryRun(boolean dryRun) {
        this.dryRun = dryRun;
    }

    public String getMessagingServiceId() {
        return messagingServiceId;
    }

    public void setMessagingServiceId(String messagingServiceId) {
        this.messagingServiceId = messagingServiceId;
    }
}
