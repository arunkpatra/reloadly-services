package com.reloadly.security.filter;

import com.reloadly.commons.model.ReloadlyCredentials;
import com.reloadly.security.model.ReloadlyUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Mock security authentication request filter.
 *
 * @author Arun Patra
 */
public class ReloadlyMockAuthenticationRequestFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReloadlyMockAuthenticationRequestFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        verifyToken(request);
        filterChain.doFilter(request, response);
    }

    private void verifyToken(HttpServletRequest request) {
        String uid = getUid(request);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Mock UserId (Run this for tests ONLY): {}", uid);
        }
        try {
            if (uid != null) {
                UserDetails userDetails = loadUserByUsername(uid);
                if (userDetails != null) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails,
                                    getCredentials(ReloadlyCredentials.CredentialType.MOCK_UID, uid),
                                    userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (UsernameNotFoundException e) {
            LOGGER.error("Exception: {}", e.getMessage());
        }
    }

    private String getUid(HttpServletRequest request) {
        String mockUid = null;
        String value = request.getHeader("X-Mock-UID");
        if (StringUtils.hasText(value)) {
            mockUid = value;
        }
        return mockUid;
    }

    private ReloadlyCredentials getCredentials(ReloadlyCredentials.CredentialType credentialType, String credential) {
        ReloadlyCredentials credentials = new ReloadlyCredentials();
        credentials.setType(credentialType);
        credentials.setCredentials(credential);
        return credentials;
    }


    public UserDetails loadUserByUsername(String uid) throws UsernameNotFoundException {
        ReloadlyUserDetails userDetails = new ReloadlyUserDetails();
        userDetails.setUid(uid);
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", uid);
        claims.put("iss", "Reloadly Mock Authentication Service");
        claims.put("aud", "reloadly-platform");
        List<String> roles = new ArrayList<>();
        roles.add("ROLE_USER");
        claims.put("roles", roles);
        userDetails.setClaims(claims);

        return userDetails;
    }
}
