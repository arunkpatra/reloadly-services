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
