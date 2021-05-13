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

package com.reloadly.transaction.controller;

import com.reloadly.commons.exceptions.ReloadlyException;
import com.reloadly.commons.model.ErrorResponse;
import com.reloadly.tracing.annotation.Traced;
import com.reloadly.transaction.model.TransactionRequest;
import com.reloadly.transaction.model.TransactionResponse;
import com.reloadly.transaction.model.TransactionStatus;
import com.reloadly.transaction.model.TransactionStatusUpdateRequest;
import com.reloadly.transaction.service.TransactionService;
import io.swagger.annotations.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Transaction Microservice REST APIs.
 *
 * @author Arun Patra
 */
@RestController
@CrossOrigin
@Api(tags = {"Transaction"}, hidden = true, value = "Transaction Services")
public class TransactionController extends AbstractRestController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @ApiOperation(value = "Post a new transaction",
            notes = "Post a new transaction", response = TransactionResponse.class,
            produces = "application/json", authorizations = {@Authorization(value = "Access Token")})
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Request Accepted",
                    response = TransactionResponse.class),
            @ApiResponse(code = 400, message = "Bad request, Rejected",
                    response = TransactionResponse.class),
            @ApiResponse(code = 500, message = "An internal error occurred.",
                    response = ErrorResponse.class)
    })
    @ResponseBody
    @PostMapping(value = "/transaction", produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    @Traced(operationName = "/transaction")
    public ResponseEntity<TransactionResponse> postTransaction(
            @ApiParam(name = "Transaction Request", required = true) @RequestBody TransactionRequest request,
            HttpServletRequest servletRequest,
            @RequestHeader HttpHeaders headers) throws ReloadlyException {

        return new ResponseEntity<>(transactionService.acceptTransaction(request), HttpStatus.ACCEPTED);
    }

    @ApiOperation(value = "Update transaction status",
            notes = "Update transaction status", response = TransactionResponse.class,
            produces = "application/json", authorizations = {@Authorization(value = "Access Token")})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Updated",
                    response = TransactionResponse.class),
            @ApiResponse(code = 404, message = "Not found",
                    response = TransactionResponse.class),
            @ApiResponse(code = 500, message = "An internal error occurred.",
                    response = ErrorResponse.class)
    })
    @ResponseBody
    @PutMapping(value = "/transaction/{txnId}", produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    @Traced(operationName = "/transaction/{txnId}")
    public ResponseEntity<TransactionResponse> updateTransactionStatus(
            @ApiParam(name = "Transaction ID", required = true) @PathVariable(name = "txnId") String txnId,
            @ApiParam(name = "Transaction Status Update Request", required = true)
            @RequestBody TransactionStatusUpdateRequest request, HttpServletRequest servletRequest,
            @RequestHeader HttpHeaders headers) throws ReloadlyException {
        // TODO: Implement me
        return new ResponseEntity<>(new TransactionResponse(txnId, request.getTransactionStatus()), HttpStatus.OK);
    }

    @ApiOperation(value = "Get Transaction Status",
            notes = "Get Transaction Status", response = TransactionResponse.class,
            produces = "application/json", authorizations = {@Authorization(value = "Access Token")})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Transaction status retrieved",
                    response = TransactionResponse.class),
            @ApiResponse(code = 404, message = "Transaction Not Found",
                    response = TransactionResponse.class),
            @ApiResponse(code = 500, message = "An internal error occurred.",
                    response = ErrorResponse.class)
    })
    @ResponseBody
    @GetMapping(value = "/transaction/{txnId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Traced(operationName = "/transaction/{txnId}")
    public ResponseEntity<TransactionResponse> getTransactionStatus(
            @ApiParam(name = "Transaction ID", required = true) @PathVariable(name = "txnId") String txnId,
            HttpServletRequest servletRequest,
            @RequestHeader HttpHeaders headers) throws ReloadlyException {
        // TODO: Implement me
        return new ResponseEntity<>(new TransactionResponse(txnId, TransactionStatus.SUCCESSFUL), HttpStatus.OK);
    }
}
