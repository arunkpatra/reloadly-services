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

package com.reloadly.commons.model;

/**
 * A credentials object that wraps a reloadly issued JWT token. This is useful when a microservice wants to communicate
 * with another. In such scenarios, the JWT token issued already, may be transmitted in the <code>Authorization</code>
 * header. The format of the header value should be "Bearer _token_", where _token_ is the JWT token string.
 * <p>
 * This object can also wrap other credentials like API keys and Mock UIDs(for tests).
 *
 * @author Arun Patra
 */
public class ReloadlyCredentials {

    /**
     * The Credentials type.
     */
    private CredentialType type;
    /**
     * The credential, usually a JWT token.
     */
    private String credentials;

    public CredentialType getType() {
        return type;
    }

    public void setType(CredentialType type) {
        this.type = type;
    }

    public String getCredentials() {
        return credentials;
    }

    public void setCredentials(String credentials) {
        this.credentials = credentials;
    }

    public enum CredentialType {
        RELOADLY_JWT_TOKEN, MOCK_UID, API_KEY
    }
}
