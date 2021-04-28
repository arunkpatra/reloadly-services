package com.reloadly.security.utils;

import com.reloadly.commons.model.ReloadlyCredentials;
import com.reloadly.security.exception.ReloadlyAuthException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;
/**
 * Security util.
 *
 * @author Arun Patra
 */
public class SecurityUtils {

    public static ReloadlyCredentials getCredentials() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Assert.notNull(auth, "Authentication object can not be null");

        Object o = auth.getCredentials();
        Assert.notNull(o, "Credentials object can not be null");

        Assert.isTrue((o instanceof ReloadlyCredentials), "Invalid credentials type. Must be an instance of " +
                "ReloadlyCredentials");

        return (ReloadlyCredentials) o;
    }
}
