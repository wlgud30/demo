package com.example.demo.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenDto {

    /**
     * 토큰 타입
     */
    private String grantType;
    /**
     * 엑세스 토큰
     */
    private String accessToken;
    /**
     * 리프레시 토큰
     */
    private String refreshToken;
    /**
     * 리프레시 토큰 만료 시간
     */
    private Long refreshTokenExpirationTime;

//    public static TokenDto of(String accessToken, String refreshToken, Long refreshTokenExpirationTime) {
//        return TokenDto.builder()
//                .grantType("Bearer")
//                .accessToken(accessToken)
//                .refreshToken(refreshToken)
//                .refreshTokenExpirationTime(refreshTokenExpirationTime)
//                .build();
//    }
}