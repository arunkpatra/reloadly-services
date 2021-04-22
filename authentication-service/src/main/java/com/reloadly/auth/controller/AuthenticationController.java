package com.reloadly.auth.controller;

import com.reloadly.auth.exception.*;
import com.reloadly.auth.model.*;
import com.reloadly.auth.service.AuthenticationService;
import com.reloadly.auth.service.UserService;
import com.reloadly.commons.exceptions.ReloadlyException;
import com.reloadly.commons.model.ReloadlyAuthToken;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;

@RestController
@CrossOrigin
@Api(tags = {"Authentication"}, hidden = true, value = "Authentication Services")
public class AuthenticationController extends AbstractRestController{

    private final AuthenticationService authenticationService;
    private final UserService userService;

    public AuthenticationController(AuthenticationService authenticationService, UserService userService) {
        this.authenticationService = authenticationService;
        this.userService = userService;
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
    public ResponseEntity<AuthenticationResponse> login(@RequestBody UsernamePasswordAuthRequest request) throws ReloadlyException {
        try {
            AuthenticationResponse authenticationResponse =
                    authenticationService.authenticateUsingUsernamePassword(request.getUsername(), request.getPassword());
            return new ResponseEntity<>(authenticationResponse, HttpStatus.OK);
        } catch (UsernameNotFoundException | AuthenticationFailedException e) {
            return new ResponseEntity<>(new AuthenticationResponse("", null, null), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            throw new ReloadlyException("An internal error has occurred", e);
        }
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
    @PostMapping(value = "/token/verify", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ReloadlyAuthToken> verifyToken(@RequestBody TokenVerificationRequest request) throws ReloadlyException {
        try {
            ReloadlyAuthToken reloadlyAuthToken =
                    authenticationService.verifyToken(request.getToken());
            return new ResponseEntity<>(reloadlyAuthToken, HttpStatus.OK);
        } catch (TokenVerificationFailedException  e) {
            return new ResponseEntity<>(new ReloadlyAuthToken(Collections.singletonMap("sub", "unauthorized")), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            throw new ReloadlyException("An internal error has occurred", e);
        }
    }
}
