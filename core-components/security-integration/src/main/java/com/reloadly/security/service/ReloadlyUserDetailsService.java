package com.reloadly.security.service;

import com.reloadly.commons.model.ReloadlyAuthToken;
import com.reloadly.security.model.ReloadlyUserDetails;
import com.reloadly.security.exception.ReloadlyAuthException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class ReloadlyUserDetailsService implements UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReloadlyUserDetailsService.class);

    private final ReloadlyAuth reloadlyAuth;

    public ReloadlyUserDetailsService(ReloadlyAuth reloadlyAuth) {
        this.reloadlyAuth = reloadlyAuth;
    }

    @Override
    public UserDetails loadUserByUsername(String token) throws UsernameNotFoundException {
        ReloadlyUserDetails user = null;
        try {
            if (token != null) {
                ReloadlyAuthToken decodedToken = reloadlyAuth.verifyToken(token);
                user = getFirebaseUserFromDecodedToken(decodedToken);
            }
        } catch (ReloadlyAuthException e) {
            LOGGER.error("Reloadly Auth Exception: {}", e.getMessage());
            throw new UsernameNotFoundException("Username not found. Root cause: " + e.getMessage(), e);
        }
        return user;
    }

    private ReloadlyUserDetails getFirebaseUserFromDecodedToken(ReloadlyAuthToken decodedToken) {
        ReloadlyUserDetails user = null;
        if (decodedToken != null) {
            user = new ReloadlyUserDetails();
            user.setUid((String) decodedToken.getClaims().get("uid"));
            user.setIssuer((String) decodedToken.getClaims().get("issuer"));
            user.setAudience((String) decodedToken.getClaims().get("audience"));
            user.setClaims((Map<String, Object>) decodedToken.getClaims().get("claims"));
        }
        return user;
    }

    private String getBearerToken(HttpServletRequest request) {
        String bearerToken = null;
        String authorization = request.getHeader("Authorization");
        if (StringUtils.hasText(authorization) && authorization.startsWith("Bearer ")) {
            bearerToken = authorization.substring(7);
        }
        return bearerToken;
    }
}
