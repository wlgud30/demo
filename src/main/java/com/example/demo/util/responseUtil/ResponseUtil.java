package com.example.demo.util.responseUtil;

import com.example.demo.dto.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ResponseUtil {

    private final Integer SUCCESS_DATA = 1;

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
                .result(SUCCESS_DATA)
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

    public ResponseEntity<ResponseDto> errorResponse(String message,Integer errorCode){
        ResponseDto res = ResponseDto.fail()
                .code(errorCode)
                .httpStatus(HttpStatus.BAD_REQUEST)
                .message(message)
                .result(0)
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
