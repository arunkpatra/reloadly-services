package com.reloadly.notification.controller;

import com.reloadly.commons.exceptions.ReloadlyException;
import com.reloadly.commons.model.EmailRequest;
import com.reloadly.commons.model.ErrorResponse;
import com.reloadly.commons.model.NotificationResponse;
import com.reloadly.commons.model.SmsRequest;
import com.reloadly.notification.service.NotificationService;
import com.reloadly.tracing.annotation.Traced;
import io.swagger.annotations.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Notification controller.
 *
 * @author Arun Patra
 */
@RestController
@CrossOrigin
@Api(tags = {"Notification"}, hidden = true, value = "Notification Services")
public class NotificationController extends AbstractRestController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @ApiOperation(value = "Send an email",
            notes = "Send an email", authorizations = {@Authorization(value = "Access Token")})
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Request accepted"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "An internal error occurred.",
                    response = ErrorResponse.class)
    })
    @ResponseBody
    @PostMapping(value = "/notification/email", consumes = {MediaType.APPLICATION_JSON_VALUE})
    @Traced(operationName = "/notification/email")
    public ResponseEntity<NotificationResponse> sendEMail(
            @ApiParam(name = "Account Creation Request", required = true) @RequestBody EmailRequest request,
            HttpServletRequest servletRequest,
            @RequestHeader HttpHeaders headers) throws ReloadlyException {
        notificationService.sendEmail(request);
        return new ResponseEntity<>(new NotificationResponse("Accepted"), HttpStatus.ACCEPTED);
    }

    @ApiOperation(value = "Send a SMS",
            notes = "Send an email", authorizations = {@Authorization(value = "Access Token")})
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Request accepted"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "An internal error occurred.",
                    response = ErrorResponse.class)
    })
    @ResponseBody
    @PostMapping(value = "/notification/sms", consumes = {MediaType.APPLICATION_JSON_VALUE})
    @Traced(operationName = "/notification/sms")
    public ResponseEntity<NotificationResponse> sendSMS(
            @ApiParam(name = "Account Creation Request", required = true) @RequestBody SmsRequest request,
            HttpServletRequest servletRequest,
            @RequestHeader HttpHeaders headers) throws ReloadlyException {
        notificationService.sendSms(request);
        return new ResponseEntity<>(new NotificationResponse("Accepted"), HttpStatus.ACCEPTED);
    }
}
