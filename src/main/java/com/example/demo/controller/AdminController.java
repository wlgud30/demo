package com.example.demo.controller;

import com.example.demo.dto.ResponseDto;
import com.example.demo.dto.TokenDto;
import com.example.demo.dto.member.AdminAddRequestDto;
import com.example.demo.dto.member.LoginDto;
import com.example.demo.enums.ExceptionEnum;
import com.example.demo.service.admin.IAdminService;
import com.example.demo.service.authentication.IAuthService;
import com.example.demo.util.responseUtil.CustomFailResponseAnnotation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/admin")
@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "AdminController",description = "관리자 관련 API")
public class AdminController {

    private final IAdminService adminService;
    private final IAuthService authService;

   @PostMapping("/join")
   @Operation(summary = "관리자 계정 생성", description = "관리자 계정을 생성합니다")
   @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ResponseDto<Long>> createAdmin(AdminAddRequestDto adminAddRequestDto){
        return adminService.addAdmin(adminAddRequestDto);
    }

    @PostMapping("/login")
    @Operation(summary = "관리자 로그인", description = "관리자 로그인을 합니다.")
    @CustomFailResponseAnnotation(exception = ExceptionEnum.BAD_REQUEST)
    @CustomFailResponseAnnotation(exception = ExceptionEnum.BAD_REQUEST2)
    @CustomFailResponseAnnotation(exception = ExceptionEnum.LOGIN_REQUIRED)
    public ResponseEntity<ResponseDto<TokenDto>> adminLogin(@RequestBody LoginDto loginDto){
        return authService.login(loginDto);
    }
}
