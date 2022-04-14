package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ResponseDto {

    private Integer code;

    private HttpStatus httpStatus;

    private String message;

    private Object result;

    @Builder(builderMethodName = "success", builderClassName = "success")
    public ResponseDto(String message, Object result) {
        this.code = 200;
        this.httpStatus = HttpStatus.OK;
        this.message = message;
        this.result = result;
    }

    @Builder(builderMethodName = "fail", builderClassName = "fail")
    public ResponseDto(Integer code, HttpStatus httpStatus, String message, Object result) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
        this.result = result;
    }
}
