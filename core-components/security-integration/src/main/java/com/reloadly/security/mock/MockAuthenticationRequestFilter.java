package com.reloadly.security.mock;

import com.reloadly.security.auth.model.ReloadlyCredentials;
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

public class MockAuthenticationRequestFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(MockAuthenticationRequestFilter.class);

    private final UserDetailsService userDetailsService;

    public MockAuthenticationRequestFilter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

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
                UserDetails userDetails = userDetailsService.loadUserByUsername(uid);
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
}
