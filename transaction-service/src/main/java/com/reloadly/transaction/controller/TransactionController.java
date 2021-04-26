package com.reloadly.transaction.controller;

import com.reloadly.commons.exceptions.ReloadlyException;
import com.reloadly.commons.model.ErrorResponse;
import com.reloadly.transaction.exception.ReloadlyTxnException;
import com.reloadly.transaction.model.TransactionRequest;
import com.reloadly.transaction.model.TransactionResponse;
import com.reloadly.transaction.model.TransactionStatus;
import com.reloadly.transaction.model.TransactionStatusUpdateRequest;
import com.reloadly.transaction.service.TransactionService;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@Api(tags = {"Transaction"}, hidden = true, value = "Transaction Services")
public class TransactionController extends AbstractRestController{

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
    @PostMapping(value = "/transaction", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<TransactionResponse> postTransaction(
            @ApiParam(name = "Transaction Request", required = true) @RequestBody TransactionRequest request) throws ReloadlyException {
        try {
            return new ResponseEntity<>(transactionService.acceptTransaction(request), HttpStatus.ACCEPTED);
        } catch (ReloadlyTxnException e) {
            return new ResponseEntity<>(new TransactionResponse("", TransactionStatus.REJECTED), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            throw new ReloadlyException("An error occurred. Root cause: ".concat(e.getMessage()), e);
        }
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
    @PutMapping(value = "/transaction/{txnId}", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<TransactionResponse> updateTransactionStatus(
            @ApiParam(name = "Transaction ID", required = true) @PathVariable(name = "txnId")String txnId,
            @ApiParam(name = "Transaction Status Update Request", required = true) @RequestBody TransactionStatusUpdateRequest request) throws ReloadlyException {

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
    public ResponseEntity<TransactionResponse> getTransactionStatus(
            @ApiParam(name = "Transaction ID", required = true) @PathVariable(name = "txnId")String txnId) throws ReloadlyException {
        return new ResponseEntity<>(new TransactionResponse(txnId, TransactionStatus.SUCCESSFUL), HttpStatus.OK
        );
    }
}
