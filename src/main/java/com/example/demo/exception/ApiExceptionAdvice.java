package com.example.demo.exception;

import com.example.demo.dto.ResponseDto;
import com.example.demo.util.responseUtil.ResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class ApiExceptionAdvice {

    private final ResponseUtil responseUtil;

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ResponseDto> exceptionHandler(ApiException e){
        if(e.getError_msg()!=null){
            log.error(e.getError_msg());
            return responseUtil.errorResponse(e.getError_msg(),e.getError().getErrorCode());
        }
        log.error(e.getError().getMessage());
        return responseUtil.errorResponse(e.getError().getMessage(),e.getError().getErrorCode());
    }
}
