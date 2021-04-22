package com.reloadly.auth.config;

import com.reloadly.auth.jwt.JwtTokenUtil;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableConfigurationProperties(AuthenticationServiceProperties.class)
@Configuration
@ComponentScan({"com.reloadly.auth"})
public class AuthenticationServiceConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtTokenUtil jwtTokenUtil(AuthenticationServiceProperties properties) {
        return new JwtTokenUtil(properties);
    }
}
