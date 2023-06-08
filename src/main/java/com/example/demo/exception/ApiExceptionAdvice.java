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
        if(e.getErrorMsg()!=null){
            log.error(e.getErrorMsg());
            return responseUtil.errorResponse(e,e.getErrorMsg());
        }
        log.error(e.getError().getMessage());
        return responseUtil.errorResponse(e);
    }
}
