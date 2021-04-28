package com.reloadly.security.filter;

import com.reloadly.commons.model.ReloadlyApiKeyIdentity;
import com.reloadly.commons.model.ReloadlyCredentials;
import com.reloadly.security.exception.ReloadlyAuthException;
import com.reloadly.security.model.ReloadlyUserDetails;
import com.reloadly.security.service.ReloadlyAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * An API Key based authentication request filter.
 *
 * @author Arun Patra
 */
public class ReloadlyApiKeyAuthenticationRequestFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReloadlyApiKeyAuthenticationRequestFilter.class);

    private final ReloadlyAuth reloadlyAuth;

    public ReloadlyApiKeyAuthenticationRequestFilter(ReloadlyAuth reloadlyAuth) {
        this.reloadlyAuth = reloadlyAuth;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        verifyApiKey(request);
        filterChain.doFilter(request, response);
    }

    private void verifyApiKey(HttpServletRequest request) {
        String apiKey = getApiKey(request);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Reloadly API Key: {}", apiKey);
        }
        try {
            if (apiKey != null) {
                UserDetails userDetails = getApiUserDetails(apiKey);
                if (userDetails != null) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails,
                                    getCredentials(ReloadlyCredentials.CredentialType.API_KEY, apiKey),
                                    userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (UsernameNotFoundException e) {
            LOGGER.error("Exception: {}", e.getMessage());
        }
    }

    private String getApiKey(HttpServletRequest request) {
        return  request.getHeader("RELOADLY-API-KEY");
    }

    private ReloadlyCredentials getCredentials(ReloadlyCredentials.CredentialType credentialType, String credential) {
        ReloadlyCredentials credentials = new ReloadlyCredentials();
        credentials.setType(credentialType);
        credentials.setCredentials(credential);
        return credentials;
    }

    private UserDetails getApiUserDetails(String apiKey) {
        ReloadlyUserDetails user = null;
        try {
            if (apiKey != null) {
                ReloadlyApiKeyIdentity keyIdentity = reloadlyAuth.verifyApiKey(apiKey);
                user = getReloadlyUserFromDecodedApiKeyDetails(keyIdentity);
            }
        } catch (ReloadlyAuthException e) {
            LOGGER.error("Reloadly Auth Exception: {}", e.getMessage());
            throw new UsernameNotFoundException("Username not found. Root cause: " + e.getMessage(), e);
        }
        return user;
    }

    private ReloadlyUserDetails getReloadlyUserFromDecodedApiKeyDetails(ReloadlyApiKeyIdentity keyIdentity) {
        ReloadlyUserDetails user = null;
        if (keyIdentity != null) {
            user = new ReloadlyUserDetails();
            user.setUid(keyIdentity.getUid());
            Map<String, Object> claims = new HashMap<>();
            claims.put("roles", keyIdentity.getRoles());
            user.setClaims(claims);
        }
        return user;
    }

}
