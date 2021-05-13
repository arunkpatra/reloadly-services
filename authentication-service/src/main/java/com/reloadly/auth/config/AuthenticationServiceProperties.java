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

package com.reloadly.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Authentication Service properties. All properties are injectable via spring properties or environment variables.
 *
 * @author Arun Patra
 */
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
