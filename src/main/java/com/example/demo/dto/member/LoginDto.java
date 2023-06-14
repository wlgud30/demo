package com.example.demo.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginDto {

    @Schema(description = "로그인에 사용 할 이메일",example = "abcd@abcd.com",requiredMode = Schema.RequiredMode.REQUIRED)
    public String email;
    @Schema(description = "로그인에 사용 할 패스워드",example = "12341234",requiredMode = Schema.RequiredMode.REQUIRED)
    public String password;

    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(email, password);
    }
}
