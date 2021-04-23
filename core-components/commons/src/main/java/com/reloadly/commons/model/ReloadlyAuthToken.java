package com.reloadly.commons.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * A decoded and verified Reloadly auth token. Can be used to get the uid and other user attributes
 * available in the token. When callers verify a Reloadly issued JWT token, they are given this decoded
 * token back.
 */
public final class ReloadlyAuthToken {

    @JsonProperty
    private final Map<String, Object> claims;

    @JsonCreator
    public ReloadlyAuthToken(Map<String, Object> claims) {
        Assert.isTrue(claims.containsKey("sub"),
                "Claims should contain the subject for the token to be valid");
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
    public Map<String, Object> getClaims() {
        return this.claims;
    }
}
