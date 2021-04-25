package com.reloadly.security.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Details of the user under whose authority, a web request is being processed. This {@link UserDetails}
 * object has certain attributes that are sourced from Reloadly Auth Service, but another implementation may
 * be used as well.
 *
 * @author Arun Patra
 */
public class ReloadlyUserDetails implements UserDetails {

    public final static String REDACTED = "[REDACTED]";
    private String uid;
    private String issuer;
    private String audience;
    private Map<String, Object> claims = new HashMap<>();

    private boolean credentialsNonExpired = true;

    private boolean enabled = true;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Object roles = claims.get("roles");
        if (null != roles) {
            List<String> roleList = (List<String>) roles;
            return roleList.stream()
                    .map(auth -> (GrantedAuthority) () -> auth).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    /**
     * Will always return the literal <code>[REDACTED]</code>.
     */
    @Override
    public String getPassword() {
        return REDACTED;
    }

    @Override
    public String getUsername() {
        return uid;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }


    public Map<String, Object> getClaims() {
        return claims;
    }

    public void setClaims(Map<String, Object> claims) {
        this.claims = claims;
    }

    public String getAudience() {
        return audience;
    }

    public void setAudience(String audience) {
        this.audience = audience;
    }
}
