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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;

import java.util.Map;

/**
 * A decoded and verified Reloadly auth token. Can be used to get the uid and other user attributes
 * available in the token. When callers verify a Reloadly issued JWT token, they are given this decoded
 * token back.
 *
 * @author Arun Patra
 */
public final class ReloadlyAuthToken {

    private final Map<String, Object> claims;

    @JsonCreator
    public ReloadlyAuthToken(Map<String, Object> claims) {
        this.claims = claims;
    }

    /**
     * Returns the Uid for the this token.
     */
    @JsonGetter
    public String getUid() {
        return (String) claims.get("sub");
    }

    /**
     * Returns the Issuer for the this token.
     */
    @JsonGetter
    public String getIssuer() {
        return (String) claims.get("iss");
    }

    /**
     * Returns the Audience for the this token.
     */
    @JsonGetter
    public String getAudience() {
        return (String) claims.get("aud");
    }

    /**
     * Returns a map of all of the claims on this token.
     */
    @JsonGetter
    public Map<String, Object> getClaims() {
        return this.claims;
    }
}
