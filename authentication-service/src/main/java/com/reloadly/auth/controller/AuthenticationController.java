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

package com.reloadly.auth.controller;

import com.reloadly.auth.model.ApiKeyVerificationRequest;
import com.reloadly.auth.model.AuthenticationResponse;
import com.reloadly.auth.model.TokenVerificationRequest;
import com.reloadly.auth.model.UsernamePasswordAuthRequest;
import com.reloadly.auth.service.AuthenticationService;
import com.reloadly.commons.exceptions.ReloadlyException;
import com.reloadly.commons.model.ErrorResponse;
import com.reloadly.commons.model.ReloadlyApiKeyIdentity;
import com.reloadly.commons.model.ReloadlyAuthToken;
import com.reloadly.tracing.annotation.Traced;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Authentication Microservice REST APIs.
 *
 * @author Arun Patra
 */
@RestController
@CrossOrigin
@Api(tags = {"Authentication"}, hidden = true, value = "Authentication Services")
public class AuthenticationController extends AbstractRestController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @ApiOperation(value = "Login using username and password",
            notes = "Login using username and password", response = AuthenticationResponse.class,
            produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Authentication successful",
                    response = AuthenticationResponse.class),
            @ApiResponse(code = 401, message = "Authentication failed",
                    response = AuthenticationResponse.class),
            @ApiResponse(code = 500, message = "An internal error occurred.",
                    response = ErrorResponse.class)
    })
    @ResponseBody
    @PostMapping(value = "/login", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    @Traced(operationName = "/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody UsernamePasswordAuthRequest request,
                                                        HttpServletRequest servletRequest,
                                                        @RequestHeader HttpHeaders headers) throws ReloadlyException {

        AuthenticationResponse authenticationResponse =
                authenticationService.authenticateUsingUsernamePassword(request.getUsername(), request.getPassword());
        return new ResponseEntity<>(authenticationResponse, HttpStatus.OK);
    }

    @ApiOperation(value = "Verify a reloadly issued JWT token",
            notes = "Verify a reloadly issued JWT token", response = ReloadlyAuthToken.class,
            produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Token verification successful",
                    response = ReloadlyAuthToken.class),
            @ApiResponse(code = 401, message = "Token verification failed",
                    response = ReloadlyAuthToken.class),
            @ApiResponse(code = 500, message = "An internal error occurred.",
                    response = ErrorResponse.class)
    })
    @ResponseBody
    @PostMapping(value = "/verify/token", produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    @Traced(operationName = "/verify/token")
    public ResponseEntity<ReloadlyAuthToken> verifyToken(@RequestBody TokenVerificationRequest request,
                                                         HttpServletRequest servletRequest,
                                                         @RequestHeader HttpHeaders headers) throws ReloadlyException {

        ReloadlyAuthToken reloadlyAuthToken = authenticationService.verifyToken(request.getToken());
        return new ResponseEntity<>(reloadlyAuthToken, HttpStatus.OK);
    }

    @ApiOperation(value = "Verify an API Key",
            notes = "Verify an API Key", response = ReloadlyApiKeyIdentity.class,
            produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "API key verification successful",
                    response = ReloadlyApiKeyIdentity.class),
            @ApiResponse(code = 401, message = "API Key verification failed",
                    response = ReloadlyAuthToken.class),
            @ApiResponse(code = 500, message = "An internal error occurred.",
                    response = ErrorResponse.class)
    })
    @ResponseBody
    @PostMapping(value = "/verify/apikey", produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    @Traced(operationName = "/verify/apikey")
    public ResponseEntity<ReloadlyApiKeyIdentity> verifyApiKey(@RequestBody ApiKeyVerificationRequest request,
                                                               HttpServletRequest servletRequest,
                                                               @RequestHeader HttpHeaders headers)
            throws ReloadlyException {

        ReloadlyApiKeyIdentity reloadlyApiKeyIdentity = authenticationService.verifyApiKey(request.getClientId(), request.getApiKey());
        return new ResponseEntity<>(reloadlyApiKeyIdentity, HttpStatus.OK);
    }
}
