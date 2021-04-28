package com.reloadly.notification.controller;

import com.reloadly.commons.exceptions.NotificationException;
import com.reloadly.commons.exceptions.ReloadlyException;
import com.reloadly.commons.model.EmailRequest;
import com.reloadly.commons.model.ErrorResponse;
import com.reloadly.commons.model.NotificationResponse;
import com.reloadly.commons.model.SmsRequest;
import com.reloadly.notification.service.NotificationService;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<NotificationResponse> sendEMail(
            @ApiParam(name = "Account Creation Request", required = true) @RequestBody EmailRequest request) throws ReloadlyException {
        try {
            notificationService.sendEmail(request);
            return new ResponseEntity<>(new NotificationResponse("Accepted"), HttpStatus.ACCEPTED);
        } catch (NotificationException e) {
            return new ResponseEntity<>(new NotificationResponse("Rejected"), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            throw new ReloadlyException("An internal error occurred. Root cause: ".concat(e.getMessage()), e);
        }
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
    public ResponseEntity<NotificationResponse> sendSMS(
            @ApiParam(name = "Account Creation Request", required = true) @RequestBody SmsRequest request) throws ReloadlyException {
        try {
            notificationService.sendSms(request);
            return new ResponseEntity<>(new NotificationResponse("Accepted"), HttpStatus.ACCEPTED);
        } catch (NotificationException e) {
            return new ResponseEntity<>(new NotificationResponse("Rejected"), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            throw new ReloadlyException("An internal error occurred. Root cause: ".concat(e.getMessage()), e);
        }
    }
}
