package com.reloadly.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The Authentication Microservice Spring Boot App.
 *
 * <p>
 *     The Authentication service allows multiple enterprise facilities like:
 *     1. User signup - Currently username and password based login is supported. Can be extended to other auth channels. Users are issued JWT tokens.
 *     2. Signup - Users can signup by choosing a username and password.
 *     3. Token Verification - Verify JWT tokens.
 *     4. API Key verification and issue functions. API keys are issued to users and service accounts.
 *     5. All other microservices call into the Auth Service to validate tokens and API keys. It is a foundational service.
 * </p>
 *
 * @author Arun Patra
 */
@SpringBootApplication
public class ReloadlyAuthenticationServiceApp {

    public static void main(String[] args) {
        SpringApplication.run(ReloadlyAuthenticationServiceApp.class, args);
    }

}
