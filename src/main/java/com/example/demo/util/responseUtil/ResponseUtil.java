package com.example.demo.util.responseUtil;

import com.example.demo.dto.ResponseDto;
import com.example.demo.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ResponseUtil {

    public ResponseEntity<ResponseDto> successResponse(String message, Object data){
        ResponseDto res = ResponseDto.success()
                .message(message)
                .result(data)
                .build();
        return createResponse(res);
    }

    public ResponseEntity<ResponseDto> successResponse(String message){
        ResponseDto res = ResponseDto.success()
                .message(message)
                .result(null)
                .build();
        return createResponse(res);
    }

    public ResponseEntity<ResponseDto> badRequestResponse(String message){
        ResponseDto res = ResponseDto.fail()
                .code(400)
                .httpStatus(HttpStatus.BAD_REQUEST)
                .message(message)
                .result(0)
                .build();
        return createResponse(res);
    }

    public ResponseEntity<ResponseDto> errorResponse(ApiException e){
        ResponseDto res = ResponseDto.fail()
                .code(e.getError().getErrorCode())
                .httpStatus(e.getError().getStatus())
                .message(e.getError().getMessage())
                .build();
        return createResponse(res);
    }

    public ResponseEntity<ResponseDto> errorResponse(ApiException e,String message){
        ResponseDto res = ResponseDto.fail()
                .code(e.getError().getErrorCode())
                .httpStatus(e.getError().getStatus())
                .message(e.getError().getMessage())
                .build();
        return createResponse(res);
    }

    public ResponseEntity<ResponseDto> tokenVerificationFail(String errorMessage){
        ResponseDto res = ResponseDto
                .fail()
                .code(401)
                .httpStatus(HttpStatus.UNAUTHORIZED)
                .message(errorMessage)
                .result(0)
                .build();
        return createResponse(res);
    }

    public ResponseEntity<ResponseDto> createResponse(ResponseDto res){
        return new ResponseEntity<>(res,res.getHttpStatus());
    }
}
