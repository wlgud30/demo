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
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ResponseDto<Void>> exceptionHandler(ApiException e){
        if(e.getErrorMsg()!=null){
            log.error(e.getErrorMsg());
            return ResponseUtil.errorResponse(e,e.getErrorMsg());
        }
        log.error(e.getError().getMessage());
        return ResponseUtil.errorResponse(e);
    }
}
