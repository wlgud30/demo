package com.example.demo.util.responseUtil;

import com.example.demo.dto.ResponseDto;
import com.example.demo.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Slf4j
public class ResponseUtil {

    public static <T> ResponseEntity<ResponseDto<T>> successResponse(String message, T data){
        ResponseDto<T> res = ResponseDto.<T>successBuilder()
                .message(message)
                .result(data)
                .build();
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    public static ResponseEntity<ResponseDto<Void>> successResponse(String message){
        ResponseDto<Void> res = ResponseDto.<Void>successBuilder()
                .message(message)
                .result(null)
                .build();
        return new ResponseEntity<>(res,HttpStatus.OK);
    }

    public static ResponseEntity<ResponseDto<Void>> errorResponse(ApiException e){
        ResponseDto<Void> res = ResponseDto.<Void>failBuilder()
                .code(e.getError().getErrorCode())
                .httpStatus(e.getError().getStatus())
                .message(e.getError().getMessage())
                .build();
        return new ResponseEntity<>(res,e.getError().getStatus());
    }

    public static ResponseEntity<ResponseDto<Void>> errorResponse(ApiException e, String message){
        ResponseDto<Void> res = ResponseDto.<Void>failBuilder()
                .code(e.getError().getErrorCode())
                .httpStatus(e.getError().getStatus())
                .message(message)
                .build();
        return new ResponseEntity<>(res,e.getError().getStatus());
    }

    private ResponseUtil() {
    }
}
