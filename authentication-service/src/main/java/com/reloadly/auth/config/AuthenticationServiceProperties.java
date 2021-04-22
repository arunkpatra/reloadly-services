package com.reloadly.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(AuthenticationServiceProperties.PREFIX)
public class AuthenticationServiceProperties {

    public static final String PREFIX = "reloadly.auth";

    private static final long DEFAULT_JWT_TOKEN_VALIDITY_DURATION_SECONDS = 5 * 60 * 60;

    private static final String DEFAULT_ISSUER = "Reloadly Authentication Service";

    private static final String DEFAULT_AUDIENCE = "reloadly-platform";

    /**
     * ENable or disable Swagger UI
     */
    private boolean swaggerUiEnabled = true;

    /**
     * JWT token validity in seconds.
     */
    private long jwtTokenValiditySeconds = DEFAULT_JWT_TOKEN_VALIDITY_DURATION_SECONDS;
    /**
     * JWT Secret key. <strong>Always inject this as an environment variables at runtime in production environments.</strong>
     */
    private String jwtSecretKey = "somesecretkey234242432fdfsdfsfsfs242432424242fsfsfs";

    private String issuer = DEFAULT_ISSUER;

    private String audience = DEFAULT_AUDIENCE;

    public String getJwtSecretKey() {
        return jwtSecretKey;
    }

    public void setJwtSecretKey(String jwtSecretKey) {
        this.jwtSecretKey = jwtSecretKey;
    }

    public long getJwtTokenValiditySeconds() {
        return jwtTokenValiditySeconds;
    }

    public void setJwtTokenValiditySeconds(long jwtTokenValiditySeconds) {
        this.jwtTokenValiditySeconds = jwtTokenValiditySeconds;
    }

    public boolean isSwaggerUiEnabled() {
        return swaggerUiEnabled;
    }

    public void setSwaggerUiEnabled(boolean swaggerUiEnabled) {
        this.swaggerUiEnabled = swaggerUiEnabled;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getAudience() {
        return audience;
    }

    public void setAudience(String audience) {
        this.audience = audience;
    }
}
