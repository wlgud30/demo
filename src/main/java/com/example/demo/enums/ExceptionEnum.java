package com.example.demo.enums;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
public enum ExceptionEnum {

    EXPIRED_JWT_EXCEPTION(HttpStatus.UNAUTHORIZED,"만료된 토큰입니다.",0),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED,"유효하지 않은 토큰 입니다.",1),
    SIGNATURE_TOKEN(HttpStatus.UNAUTHORIZED,"유효하지 않은 토큰 입니다.",2),
    NOT_FOUND_TOKEN(HttpStatus.UNAUTHORIZED,"유효하지 않은 토큰 입니다.",3),
    BAD_REQUEST(HttpStatus.BAD_REQUEST,4),
    NO_AUTHENTICATION_INFORMATION(HttpStatus.BAD_REQUEST,"인증정보가 존재하지 않습니다.",5),
    ;

    private final HttpStatus status;
    private String message;
    private final Integer errorCode;

    ExceptionEnum(HttpStatus status,Integer errorCode){
        this.status=status;
        this.errorCode=errorCode;
    }

    ExceptionEnum(HttpStatus status, String message,Integer errorCode){
        this.status = status;
        this.message = message;
        this.errorCode = errorCode;
    }
}
