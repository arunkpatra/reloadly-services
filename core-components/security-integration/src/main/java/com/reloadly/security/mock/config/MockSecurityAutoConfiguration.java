package com.reloadly.security.mock.config;

import com.reloadly.security.mock.MockAuthenticationEntryPoint;
import com.reloadly.security.mock.MockAuthenticationRequestFilter;
import com.reloadly.security.mock.MockUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(MockSecurityProperties.class)
@ConditionalOnProperty(name = "meelu.security.mock.enabled")
public class MockSecurityAutoConfiguration {

    @EnableWebSecurity
    @EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
    @ConditionalOnProperty(name = "reloadly.auth.mock.enabled")
    public static class MockWebSecurityConfig extends WebSecurityConfigurerAdapter {

        private static final Logger LOGGER = LoggerFactory.getLogger(MockWebSecurityConfig.class);

        private final MockAuthenticationRequestFilter mockAuthenticationRequestFilter;

        public MockWebSecurityConfig(MockAuthenticationRequestFilter mockAuthenticationRequestFilter) {
            this.mockAuthenticationRequestFilter = mockAuthenticationRequestFilter;
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
                LOGGER.info("Enabling Mock Auth based HTTP Security.");
                //@formatter:off
                http
                        .csrf().disable()
                        .authorizeRequests()
                        .antMatchers("/assets/**").permitAll()
                        .antMatchers("/instances").permitAll()
                        .antMatchers("/instances/*").permitAll()
                        .antMatchers("/actuator/**").permitAll()
                        .antMatchers("/auth/login").permitAll()
                        .antMatchers("/swagger-ui/**").permitAll()
                        .antMatchers("/swagger**").permitAll()
                        .antMatchers("/v2/api-docs/**").permitAll()
                        .antMatchers("/swagger-resources/configuration/**").permitAll()
                        .antMatchers("/webjars/**").permitAll()
                        .antMatchers("/api/signup/mobile").permitAll()
                        .antMatchers("/api/signup/email").permitAll()
                        .antMatchers("/api/otp").permitAll()
                        .antMatchers("/api/invitation/joinqueue").permitAll()
                        .antMatchers("/api/user/sync").authenticated()
                        .antMatchers("/api/**").hasAnyRole("USER")
                        .anyRequest().authenticated().and()
                        .exceptionHandling()
                        .authenticationEntryPoint(new MockAuthenticationEntryPoint()).and().sessionManagement()
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                //@formatter:on
                // Add a filter to validate the tokens with every request
                http.addFilterBefore(mockAuthenticationRequestFilter, UsernamePasswordAuthenticationFilter.class);

        }

        @Configuration
        @ConditionalOnProperty(name = "reloadly.auth.mock.enabled")
        public static class SecurityConfigInner {

            @Bean
            @ConditionalOnMissingBean(UserDetailsService.class)
            public UserDetailsService customUserDetailsService() {
                return new MockUserDetailsService();
            }

            @Bean
            public MockAuthenticationRequestFilter mockAuthenticationRequestFilter(UserDetailsService userDetailsService) {
                return new MockAuthenticationRequestFilter(userDetailsService);
            }
        }
    }
}
