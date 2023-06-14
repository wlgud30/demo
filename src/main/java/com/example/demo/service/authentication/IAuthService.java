package com.example.demo.service.authentication;

import com.example.demo.dto.ResponseDto;
import com.example.demo.dto.TokenDto;
import com.example.demo.dto.member.LoginDto;
import org.springframework.http.ResponseEntity;

public interface IAuthService {
    /**
     * @param loginDto 로그인에 사용할 이메일과 패스워드
     * @return 로그인 성공 시 토큰 발급
    */
    ResponseEntity<ResponseDto<TokenDto>> login(LoginDto loginDto);
    /**
     * @param dto 토큰 재발급에 사용할 토큰
     * @return 토큰 재발급 성공 시 새로운 토큰 발급
    */
    ResponseEntity<ResponseDto<TokenDto>> reissue(TokenDto dto);
    /**
     * @param logout 로그아웃에 사용할 토큰
     * @return 로그아웃 성공 시 토큰 무효화
    */
    ResponseEntity<ResponseDto<Void>> logout(TokenDto logout);
}
