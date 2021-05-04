package com.reloadly.commons.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Error message from REST API when unhandled errors occur.
 *
 * @author Arun Patra
 */
public class ErrorResponse {

    private final String error;

    private final String errorDescription;

    private final String errorDetail;

    @JsonCreator
    public ErrorResponse(@JsonProperty("error") String error, @JsonProperty("error_description") String errorDescription,
                         @JsonProperty("error_detail") String errorDetail) {
        this.error = error;
        this.errorDescription = errorDescription;
        this.errorDetail = errorDetail;
    }

    public String getError() {
        return error;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public String getErrorDetail() {
        return errorDetail;
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "error='" + error + '\'' +
                ", errorDescription='" + errorDescription + '\'' +
                ", errorDetail='" + errorDetail + '\'' +
                '}';
    }
}
