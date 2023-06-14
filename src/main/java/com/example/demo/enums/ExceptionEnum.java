package com.example.demo.enums;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
public enum ExceptionEnum {

    /**
     * 에러 목록
     * 10xx : 토큰과 관련된 예외
     * 40xx : 잘못된 요청
     * 41xx : 인증과 관련된 예외
     * 44xx : 리소스를 찾을 수 없는 예외
     * 50xx : 서버 내부 오류
     */
    EXPIRED_JWT_EXCEPTION(HttpStatus.UNAUTHORIZED,"만료된 토큰입니다.",1000),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED,"유효하지 않은 토큰 입니다.",1001),
    SIGNATURE_TOKEN(HttpStatus.UNAUTHORIZED,"서명을 확인 할 수 없습니다.",1002),
    NOT_FOUND_TOKEN(HttpStatus.UNAUTHORIZED,"토큰을 찾을 수 없습니다.",1003),
    CONFLICT_TOKEN(HttpStatus.UNAUTHORIZED,"요청한 토큰과 값이 다릅니다.",1004),
    BAD_REQUEST(HttpStatus.BAD_REQUEST,"잘못된 요청입니다.",4000),
    BAD_REQUEST2(HttpStatus.BAD_REQUEST,"잘못된 요청입니다.2",4001),
    NO_AUTHENTICATION_INFORMATION(HttpStatus.UNAUTHORIZED,"인증정보가 존재하지 않습니다.",4100),
    LOGIN_REQUIRED(HttpStatus.UNAUTHORIZED,"로그인이 필요합니다.",4101),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED,"비밀번호를 확인해주세요.",4101),
    CONFLICT_EMAIL(HttpStatus.CONFLICT,"중복되는 이메일입니다.",4900),
    NOT_FOUND_ADMIN(HttpStatus.UNAUTHORIZED,"관리자를 찾을 수 없습니다.",4400),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"서버에 오류가 발생하였습니다.",5000),
    ;

    private final HttpStatus status;
    private String message;
    private final Integer errorCode;

    ExceptionEnum(HttpStatus status, String message,Integer errorCode){
        this.status = status;
        this.message = message;
        this.errorCode = errorCode;
    }
}
