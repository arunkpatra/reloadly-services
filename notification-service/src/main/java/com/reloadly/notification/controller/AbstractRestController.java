package com.reloadly.notification.controller;

import com.reloadly.commons.controller.BaseAbstractRestController;
import com.reloadly.commons.exceptions.NotificationException;
import com.reloadly.commons.model.NotificationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * An abstract REST Controller exposing useful facilities for concrete implementation to leverage.
 *
 * @author Arun Patra
 */
public abstract class AbstractRestController extends BaseAbstractRestController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NotificationException.class)
    public NotificationResponse handleNotificationException(NotificationException e) {
        return new NotificationResponse("Rejected");
    }
}
