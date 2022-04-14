package com.example.demo.exception;

import com.example.demo.enums.ExceptionEnum;
import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {
    private final ExceptionEnum error;
    private String error_msg;

    public ApiException(ExceptionEnum error, String error_msg) {
        this.error = error;
        this.error_msg = error_msg;
    }

    public ApiException(ExceptionEnum error) {
        this.error = error;
    }
}