package com.reloadly.security.mock;

import com.reloadly.security.auth.model.ReloadlyUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MockUserDetailsService implements UserDetailsService {
    @Override
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
