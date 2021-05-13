/*
 * MIT License
 *
 * Copyright (c) 2021 Arun Patra
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.reloadly.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The Authentication Microservice Spring Boot App.
 *
 * <p>
 * The Authentication service allows multiple enterprise facilities like:
 * 1. User signup - Currently username and password based login is supported. Can be extended to other auth channels. Users are issued JWT tokens.
 * 2. Signup - Users can signup by choosing a username and password.
 * 3. Token Verification - Verify JWT tokens.
 * 4. API Key verification and issue functions. API keys are issued to users and service accounts.
 * 5. All other microservices call into the Auth Service to validate tokens and API keys. It is a foundational service.
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
