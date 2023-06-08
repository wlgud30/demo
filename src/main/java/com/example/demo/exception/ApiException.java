package com.example.demo.exception;

import com.example.demo.enums.ExceptionEnum;
import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {
    private final ExceptionEnum error;
    private final String errorMsg;

    public ApiException(ExceptionEnum error, String errorMsg) {
        this.error = error;
        this.errorMsg = errorMsg;
    }

    public ApiException(ExceptionEnum error) {
        this.error = error;
        this.errorMsg = error.getMessage();
    }
}