package com.reloadly.account.controller;

import com.reloadly.account.model.AccountBalance;
import com.reloadly.account.model.AccountDetails;
import com.reloadly.account.model.AccountUpdateRequest;
import com.reloadly.account.model.AccountUpdateResponse;
import com.reloadly.account.service.AccountService;
import com.reloadly.commons.exceptions.ReloadlyException;
import com.reloadly.commons.model.ErrorResponse;
import com.reloadly.commons.model.account.*;
import io.swagger.annotations.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Account Microservice REST APIs.
 *
 * @author Arun Patra
 */
@RestController
@CrossOrigin
@Api(tags = {"Account"}, hidden = true, value = "Account Services")
public class AccountController extends AbstractRestController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @ApiOperation(value = "Create a new account",
            notes = "Create a new account", response = AccountUpdateResponse.class,
            produces = "application/json", authorizations = {@Authorization(value = "Access Token")})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Account created",
                    response = AccountUpdateResponse.class),
            @ApiResponse(code = 400, message = "Bad request",
                    response = AccountUpdateResponse.class),
            @ApiResponse(code = 500, message = "An internal error occurred.",
                    response = ErrorResponse.class)
    })
    @ResponseBody
    @PostMapping(value = "/account/{uid}", produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<AccountUpdateResponse> createAccount(
            @RequestBody AccountUpdateRequest request,
            @PathVariable(name = "uid") String uid, HttpServletRequest servletRequest,
            @RequestHeader HttpHeaders headers) throws ReloadlyException {
        return new ResponseEntity<>(accountService.updateAccount(uid, request), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Update Account",
            notes = "Update Account", response = AccountUpdateResponse.class,
            consumes = "application/json",
            produces = "application/json", authorizations = {@Authorization(value = "Access Token")})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Account updated",
                    response = AccountUpdateResponse.class),
            @ApiResponse(code = 400, message = "Bad request",
                    response = AccountUpdateResponse.class),
            @ApiResponse(code = 500, message = "An internal error occurred.",
                    response = ErrorResponse.class)
    })
    @ResponseBody
    @PutMapping(value = "/account/{uid}", produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<AccountUpdateResponse> updateAccount(
            @RequestBody AccountUpdateRequest request,
            @PathVariable(name = "uid") String uid, HttpServletRequest servletRequest,
            @RequestHeader HttpHeaders headers) throws ReloadlyException {
        return new ResponseEntity<>(accountService.updateAccount(uid, request), HttpStatus.OK);
    }

    @ApiOperation(value = "Get Account details",
            notes = "Get Account details", response = AccountDetails.class,
            produces = "application/json", authorizations = {@Authorization(value = "Access Token")})
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
            @PathVariable(name = "uid") String uid, HttpServletRequest servletRequest,
            @RequestHeader HttpHeaders headers) throws ReloadlyException {
        return new ResponseEntity<>(accountService.getAccountDetails(uid), HttpStatus.OK);
    }

    @ApiOperation(value = "Get Account Balance",
            notes = "Get Account Balance", response = AccountBalance.class,
            produces = "application/json", authorizations = {@Authorization(value = "Access Token")})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Account details found",
                    response = AccountBalance.class),
            @ApiResponse(code = 400, message = "Bad Request",
                    response = AccountBalance.class),
            @ApiResponse(code = 500, message = "An internal error occurred.",
                    response = ErrorResponse.class)
    })
    @ResponseBody
    @GetMapping(value = "/account/balance/{uid}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<AccountBalance> getAccountBalance(@PathVariable(name = "uid") String uid,
                                                            HttpServletRequest servletRequest,
                                                            @RequestHeader HttpHeaders headers)
            throws ReloadlyException {
        return new ResponseEntity<>(accountService.getAccountBalance(uid), HttpStatus.OK);
    }

    @ApiOperation(value = "Credit Account",
            notes = "Credit Account", response = AccountCreditResp.class,
            consumes = "application/json",
            produces = "application/json", authorizations = {@Authorization(value = "Access Token")})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Account updated",
                    response = AccountCreditResp.class),
            @ApiResponse(code = 400, message = "Bad request",
                    response = AccountCreditResp.class),
            @ApiResponse(code = 500, message = "An internal error occurred.",
                    response = ErrorResponse.class)
    })
    @ResponseBody
    @PostMapping(value = "/account/credit/{uid}", consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<AccountCreditResp> creditAccountBalance(@PathVariable(name = "uid") String uid,
                                                                  @RequestBody AccountCreditReq request,
                                                                  HttpServletRequest servletRequest,
                                                                  @RequestHeader HttpHeaders headers)
            throws ReloadlyException {
        return new ResponseEntity<>(accountService.creditAccountBalance(uid, request), HttpStatus.OK);
    }

    @ApiOperation(value = "Debit Account",
            notes = "Debit Account", response = AccountDebitResp.class,
            consumes = "application/json",
            produces = "application/json", authorizations = {@Authorization(value = "Access Token")})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Account debited",
                    response = AccountDebitResp.class),
            @ApiResponse(code = 400, message = "Bad request",
                    response = AccountDebitResp.class),
            @ApiResponse(code = 500, message = "An internal error occurred.",
                    response = ErrorResponse.class)
    })
    @ResponseBody
    @PostMapping(value = "/account/debit/{uid}", consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<AccountDebitResp> debitAccountBalance(@PathVariable(name = "uid") String uid,
                                                                @RequestBody AccountDebitReq request,
                                                                HttpServletRequest servletRequest,
                                                                @RequestHeader HttpHeaders headers)
            throws ReloadlyException {
        return new ResponseEntity<>(accountService.debitAccountBalance(uid, request), HttpStatus.OK);
    }

    @ApiOperation(value = "Get Account Info",
            notes = "Get Account Info", response = AccountInfo.class,
            produces = "application/json", authorizations = {@Authorization(value = "Access Token")})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Account debited",
                    response = AccountInfo.class),
            @ApiResponse(code = 404, message = "Not Found",
                    response = AccountInfo.class),
            @ApiResponse(code = 500, message = "An internal error occurred.",
                    response = ErrorResponse.class)
    })
    @ResponseBody
    @GetMapping(value = "/account/info/{uid}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<AccountInfo> getAccountInfo(
            @PathVariable(name = "uid") String uid, HttpServletRequest servletRequest,
            @RequestHeader HttpHeaders headers) throws ReloadlyException {
        return new ResponseEntity<>(accountService.getAccountInfo(uid), HttpStatus.OK);
    }
}
