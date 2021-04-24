package com.reloadly.account.controller;

import com.reloadly.account.exception.AccountNotFoundException;
import com.reloadly.account.exception.AccountUpdatedException;
import com.reloadly.account.model.AccountDetails;
import com.reloadly.account.model.AccountUpdateResponse;
import com.reloadly.account.model.AccountUpdateRequest;
import com.reloadly.account.service.AccountService;
import com.reloadly.commons.exceptions.ReloadlyException;
import com.reloadly.commons.model.ErrorResponse;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@Api(tags = {"Account"}, hidden = true, value = "Account Services")
public class AccountController extends AbstractRestController{

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @ApiOperation(value = "Create a new account",
            notes = "Create a new account", response = AccountUpdateResponse.class,
            produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Account created",
                    response = AccountUpdateResponse.class),
            @ApiResponse(code = 400, message = "Bad request",
                    response = AccountUpdateResponse.class),
            @ApiResponse(code = 500, message = "An internal error occurred.",
                    response = ErrorResponse.class)
    })
    @ResponseBody
    @PostMapping(value = "/account/{uid}", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<AccountUpdateResponse> createAccount(
            @ApiParam(name = "Account Creation Request", required = true) @RequestBody AccountUpdateRequest  request,
            @ApiParam(name = "UID of the user", required = true) @PathVariable(name = "uid")String uid) throws ReloadlyException {
        try {
            return new ResponseEntity<>(accountService.updateAccount(uid, request), HttpStatus.CREATED);
        } catch (AccountUpdatedException e) {
            return new ResponseEntity<>(new AccountUpdateResponse(false, "", "Account update failed"),
                    HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            throw new ReloadlyException("An error occurred. Root cause: " + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "Update Account",
            notes = "Update Account", response = AccountUpdateResponse.class,
            produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Account updated",
                    response = AccountUpdateResponse.class),
            @ApiResponse(code = 400, message = "Bad request",
                    response = AccountUpdateResponse.class),
            @ApiResponse(code = 500, message = "An internal error occurred.",
                    response = ErrorResponse.class)
    })
    @ResponseBody
    @PutMapping(value = "/account/{uid}", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<AccountUpdateResponse> updateAccount(
            @ApiParam(name = "Account Creation Request", required = true) @RequestBody AccountUpdateRequest  request,
            @ApiParam(name = "UID of the user", required = true) @PathVariable(name = "uid")String uid) throws ReloadlyException {
        try {
            return new ResponseEntity<>(accountService.updateAccount(uid, request), HttpStatus.OK);
        } catch (AccountUpdatedException e) {
            return new ResponseEntity<>(new AccountUpdateResponse(false, "", "Account update failed"),
                    HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            throw new ReloadlyException("An error occurred. Root cause: " + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "Get Account details",
            notes = "Get Account details", response = AccountDetails.class,
            produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Account details found",
                    response = AccountDetails.class),
            @ApiResponse(code = 404, message = "Account Not Found",
                    response = AccountDetails.class),
            @ApiResponse(code = 500, message = "An internal error occurred.",
                    response = ErrorResponse.class)
    })
    @ResponseBody
    @GetMapping(value = "/account/{uid}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<AccountDetails> getAccountDetails(
            @ApiParam(name = "UID of the user", required = true) @PathVariable(name = "uid")String uid) throws ReloadlyException {
        try {
            return new ResponseEntity<>(accountService.getAccountDetails(uid), HttpStatus.OK);
        } catch (AccountNotFoundException e) {
            return new ResponseEntity<>(new AccountDetails(null, null, null, null),
                    HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            throw new ReloadlyException("An error occurred. Root cause: " + e.getMessage(), e);
        }
    }
}
