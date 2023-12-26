package com.act.HR_management.Exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionResponse {
    private String message;
    private int errorCode;
    private String timestamp;
    private String stackTrace;

    public ExceptionResponse(String message, int errorCode, String timestamp) {
        this.message = message;
        this.errorCode = errorCode;
        this.timestamp = timestamp;
    }

    public ExceptionResponse(String message) {
        this.message = message;
    }
}
