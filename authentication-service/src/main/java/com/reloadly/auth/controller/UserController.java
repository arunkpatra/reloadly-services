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

import com.reloadly.auth.model.SignupResponse;
import com.reloadly.auth.model.UsernamePasswordSignupRequest;
import com.reloadly.auth.service.UserService;
import com.reloadly.commons.exceptions.ReloadlyException;
import com.reloadly.commons.model.ErrorResponse;
import com.reloadly.commons.model.user.UserInfo;
import com.reloadly.tracing.annotation.Traced;
import io.swagger.annotations.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * User specific REST APIs.
 *
 * @author Arun Patra
 */
@RestController
@CrossOrigin
@Api(tags = {"Users"}, hidden = true, value = "User Services")
public class UserController extends AbstractRestController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "Signup using username and password",
            notes = "Signup using username and password", response = SignupResponse.class,
            produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Signup successful",
                    response = SignupResponse.class),
            @ApiResponse(code = 400, message = "Signup failed",
                    response = SignupResponse.class),
            @ApiResponse(code = 500, message = "An internal error occurred.",
                    response = ErrorResponse.class)
    })
    @ResponseBody
    @PostMapping(value = "/signup/username", produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    @Traced(operationName = "/signup/username")
    public ResponseEntity<SignupResponse> signupUsingUsernamePassword(@RequestBody UsernamePasswordSignupRequest request,
                                                                      HttpServletRequest servletRequest,
                                                                      @RequestHeader HttpHeaders headers)
            throws ReloadlyException {
        String uid = userService.createUserForUsernamePassword(request.getUsername(), request.getPassword());
        return new ResponseEntity<>(new SignupResponse("Signup successful", uid), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Get User Info",
            notes = "Get User Info", response = UserInfo.class,
            produces = "application/json", authorizations = {@Authorization(value = "Access Token")})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK",
                    response = UserInfo.class),
            @ApiResponse(code = 404, message = "User not found",
                    response = UserInfo.class),
            @ApiResponse(code = 500, message = "An internal error occurred.",
                    response = ErrorResponse.class)
    })
    @ResponseBody
    @GetMapping(value = "/me", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Traced(operationName = "/me")
    public ResponseEntity<UserInfo> getUserInfo(HttpServletRequest servletRequest,
                                                @ApiParam(hidden = true) @RequestHeader HttpHeaders headers)
            throws ReloadlyException {
        return new ResponseEntity<>(userService.getUserInfo(headers), HttpStatus.OK);
    }
}
