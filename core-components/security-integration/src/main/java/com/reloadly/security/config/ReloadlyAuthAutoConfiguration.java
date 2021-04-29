package com.reloadly.security.config;

import com.reloadly.security.filter.ReloadlyApiKeyAuthenticationRequestFilter;
import com.reloadly.security.filter.ReloadlyJwtAuthenticationRequestFilter;
import com.reloadly.security.filter.ReloadlyMockAuthenticationRequestFilter;
import com.reloadly.security.service.ReloadlyAuth;
import com.reloadly.security.service.ReloadlyAuthServiceImpl;
import com.reloadly.security.service.ReloadlyUserDetailsService;
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
import org.springframework.web.client.RestTemplate;

/**
 * Authentication Service auto-configuration class.
 *
 * @author Arun Patra
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(ReloadlyAuthProperties.class)
public class ReloadlyAuthAutoConfiguration {

    @EnableWebSecurity
    @EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
    @ConditionalOnProperty(name = {"reloadly.auth.enabled"}, matchIfMissing = true)
    public static class ReloadlyAuthWebSecurityConfig extends WebSecurityConfigurerAdapter {

        private static final Logger LOGGER = LoggerFactory.getLogger(ReloadlyAuthWebSecurityConfig.class);

        private final ReloadlyAuthProperties properties;

        private final ReloadlyJwtAuthenticationRequestFilter reloadlyJwtRequestFilter;

        private final ReloadlyApiKeyAuthenticationRequestFilter apiKeyAuthenticationRequestFilter;

        private final ReloadlyMockAuthenticationRequestFilter reloadlyMockAuthenticationRequestFilter;

        public ReloadlyAuthWebSecurityConfig(ReloadlyAuthProperties properties, ReloadlyJwtAuthenticationRequestFilter reloadlyJwtRequestFilter,
                                             ReloadlyApiKeyAuthenticationRequestFilter apiKeyAuthenticationRequestFilter, ReloadlyMockAuthenticationRequestFilter reloadlyMockAuthenticationRequestFilter) {
            this.properties = properties;
            this.reloadlyJwtRequestFilter = reloadlyJwtRequestFilter;
            this.apiKeyAuthenticationRequestFilter = apiKeyAuthenticationRequestFilter;
            this.reloadlyMockAuthenticationRequestFilter = reloadlyMockAuthenticationRequestFilter;
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            LOGGER.info("Enabling Auth service based HTTP Security.");
            //@formatter:off
            http
                    .csrf().disable()
                    .authorizeRequests()
                    .antMatchers("/assets/**").permitAll()
                    .antMatchers("/instances").permitAll()
                    .antMatchers("/instances/*").permitAll()
                    .antMatchers("/actuator/**").permitAll()
                    .antMatchers("/swagger-ui/**").permitAll()
                    .antMatchers("/swagger**").permitAll()
                    .antMatchers("/v2/api-docs/**").permitAll()
                    .antMatchers("/favicon.ico").permitAll()
                    .antMatchers("/swagger-resources/configuration/**").permitAll()
                    .antMatchers("/webjars/**").permitAll()
                    .antMatchers("/**").hasAnyRole("USER").anyRequest().authenticated()
                    .and()
                    .exceptionHandling()
                    .authenticationEntryPoint(new ReloadlyAuthenticationEntryPoint()).and().sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            //@formatter:on
            // Add a filter to validate the tokens with every request
            http
                    .addFilterBefore(reloadlyJwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                    .addFilterBefore(apiKeyAuthenticationRequestFilter, UsernamePasswordAuthenticationFilter.class);
            if (properties.isMock()) {
                http
                        .addFilterBefore(reloadlyMockAuthenticationRequestFilter, UsernamePasswordAuthenticationFilter.class);
            }
        }

        @Configuration
        @ConditionalOnProperty(name = {"reloadly.auth.enabled"}, matchIfMissing = true)
        public static class SecurityConfigInner {

            @Bean
            @ConditionalOnMissingBean
            public RestTemplate restTemplate() {
                return new RestTemplate();
            }

            @Bean
            public ReloadlyAuth reloadlyAuthService(ReloadlyAuthProperties properties, RestTemplate restTemplate) {
                return new ReloadlyAuthServiceImpl(properties, restTemplate);
            }

            @Bean
            public UserDetailsService customUserDetailsService(ReloadlyAuth reloadlyAuth) {
                return new ReloadlyUserDetailsService(reloadlyAuth);
            }

            @Bean
            public ReloadlyJwtAuthenticationRequestFilter reloadlyJwtRequestFilter(UserDetailsService userDetailsService) {
                return new ReloadlyJwtAuthenticationRequestFilter(userDetailsService);
            }

            @Bean
            public ReloadlyApiKeyAuthenticationRequestFilter apiKeyAuthenticationRequestFilter(ReloadlyAuth reloadlyAuth) {
                return new ReloadlyApiKeyAuthenticationRequestFilter(reloadlyAuth);
            }

            @Bean
            public ReloadlyMockAuthenticationRequestFilter mockAuthenticationRequestFilter() {
                return new ReloadlyMockAuthenticationRequestFilter();
            }
        }
    }
}
