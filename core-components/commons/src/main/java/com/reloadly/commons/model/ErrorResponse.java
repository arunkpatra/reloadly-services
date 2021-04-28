package com.reloadly.commons.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Error message from REST API when unhandled errors occur.
 *
 * @author Arun Patra
 */
public class ErrorResponse {

    @JsonProperty("error")
    private final String error;

    @JsonProperty("error_description")
    private final String errorDescription;

    @JsonProperty("error_detail")
    private final String errorDetail;

    @JsonCreator
    public ErrorResponse(String error, String errorDescription, String errorDetail) {
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
