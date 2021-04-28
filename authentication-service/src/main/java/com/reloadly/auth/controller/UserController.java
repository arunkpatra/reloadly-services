package com.reloadly.auth.controller;

import com.reloadly.auth.exception.InvalidPasswordFormatException;
import com.reloadly.auth.exception.UsernameAlreadyTakenException;
import com.reloadly.commons.model.ErrorResponse;
import com.reloadly.auth.model.SignupResponse;
import com.reloadly.auth.model.UsernamePasswordSignupRequest;
import com.reloadly.auth.service.UserService;
import com.reloadly.commons.exceptions.ReloadlyException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping(value = "/signup/username", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<SignupResponse> signupUsingUsernamePassword(@RequestBody UsernamePasswordSignupRequest request) throws ReloadlyException {
        try {
            String uid = userService.createUserForUsernamePassword(request.getUsername(), request.getPassword());
            return new ResponseEntity<>(new SignupResponse("Signup successful", uid), HttpStatus.CREATED);
        } catch (UsernameAlreadyTakenException | InvalidPasswordFormatException e) {
            return new ResponseEntity<>(new SignupResponse(String.format("Signup failed. Reason: %s", e.getMessage()), null), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            throw new ReloadlyException("An internal error has occurred", e);
        }
    }
}
