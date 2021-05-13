package com.reloadly.auth.controller;

import com.reloadly.auth.exception.*;
import com.reloadly.commons.controller.BaseAbstractRestController;
import com.reloadly.commons.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * An abstract REST Controller exposing useful facilities for concrete implementation to leverage.
 *
 * @author Arun Patra
 */
public abstract class AbstractRestController extends BaseAbstractRestController {

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({UsernameNotFoundException.class, AuthenticationFailedException.class, TokenVerificationFailedException.class, ApiKeyVerificationFailedException.class})
    public ErrorResponse handleAuthException(Exception e) {
        return new ErrorResponse("Authentication exception.", "", "");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({UsernameAlreadyTakenException.class, InvalidPasswordFormatException.class, UserInfoBadRequestException.class})
    public ErrorResponse handleUserException(Exception e) {
        return new ErrorResponse(e.getMessage(), "", "");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({UserNotFoundException.class})
    public ErrorResponse handleUserNotFoundException(UserNotFoundException e) {
        return new ErrorResponse(e.getMessage(), "", "");
    }

}
