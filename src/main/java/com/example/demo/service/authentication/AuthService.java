package com.example.demo.service.authentication;

import com.example.demo.config.security.CustomAdminDetailService;
import com.example.demo.config.security.SecurityUtil;
import com.example.demo.domain.Admin;
import com.example.demo.dto.ResponseDto;
import com.example.demo.dto.TokenDto;
import com.example.demo.dto.member.LoginDto;
import com.example.demo.enums.ExceptionEnum;
import com.example.demo.exception.ApiException;
import com.example.demo.repository.admin.AdminRepository;
import com.example.demo.util.jwtUtil.JwtTokenUtil;
import com.example.demo.util.redis.RedisUtil;
import com.example.demo.util.responseUtil.ResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

import static com.example.demo.enums.ExceptionEnum.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService implements IAuthService {

    private final PasswordEncoder passwordEncoder;
    private final AdminRepository adminRepository;
    private final CustomAdminDetailService customAdminDetailService;
    private final JwtTokenUtil jwtTokenUtil;
    private final RedisUtil redisUtil;

    @Transactional
    @Override
    public ResponseEntity<ResponseDto<TokenDto>> login(LoginDto loginDto) {
        Admin admin = adminRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new ApiException(NOT_FOUND_ADMIN));
        checkPassword(loginDto.getPassword(),admin.getPassword());
        // 1. Login ID/PW 를 기반으로 Authentication 객체 생성
        // 이때 authentication 는 인증 여부를 확인하는 authenticated 값이 false
//        UsernamePasswordAuthenticationToken authenticationToken = loginDto.toAuthentication();
//        UsernamePasswordAuthenticationToken authenticationToken = customMemberDetailService.loadUserByUsername(loginDto.getEmail());
        // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분

//        UserDetails userDetails = customMemberDetailService.loadUserByUsername(loginDto.getEmail());
//        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());
//        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        // authenticate 매서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행
        Authentication authentication = getAuthentication(loginDto.getEmail());
        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenDto tokenInfo = jwtTokenUtil.generateToken(authentication);

        // 4. RefreshToken Redis 저장 (expirationTime 설정을 통해 자동 삭제 처리)
        redisUtil.setData("refreshToken:" + authentication.getName(), tokenInfo.getRefreshToken(), tokenInfo.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);

        return ResponseUtil.successResponse("로그인에 성공했습니다.",tokenInfo);
    }

    @Override
    public ResponseEntity<ResponseDto<TokenDto>> reissue(TokenDto dto) {
        // 1. Refresh Token 검증
        if (!jwtTokenUtil.validateToken(dto.getRefreshToken())) {
            throw new ApiException(ExceptionEnum.INVALID_TOKEN);
        }
        // 2. Access Token 에서 User email 를 가져옵니다.
        Authentication authentication = jwtTokenUtil.getAuthentication(dto.getAccessToken());
        // 3. Redis 에서 User email 을 기반으로 저장된 Refresh Token 값을 가져옵니다.
        checkRefreshToken(authentication.getName(),dto.getRefreshToken());
        // 4. 새로운 토큰 생성
        TokenDto tokenInfo = jwtTokenUtil.generateToken(authentication);
        // 5. RefreshToken Redis 업데이트
        redisUtil.setData("refreshToken:" + authentication.getName(), tokenInfo.getRefreshToken(), tokenInfo.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);

        return ResponseUtil.successResponse("토큰을 갱신합니다.",tokenInfo);
    }

    @Override
    public ResponseEntity<ResponseDto<Void>> logout(TokenDto tokenDto) {
        // 2. Access Token 에서 User email 을 가져옵니다.
        var username = SecurityUtil.getCurrentUser().getUsername();
        // 3. Redis 에서 해당 User email 로 저장된 Refresh Token 이 있는지 여부를 확인 후 있을 경우 삭제합니다.
        if (redisUtil.getData("refreshToken:" + username, String.class).isPresent()) {
            // Refresh Token 삭제
            redisUtil.deleteData("refreshToken:" + username);
        }

        // 4. 해당 Access Token 유효시간 가지고 와서 BlackList 로 저장하기
        Long expiration = jwtTokenUtil.getExpiration(tokenDto.getAccessToken());
        redisUtil.setData(tokenDto.getAccessToken(), "logout", expiration, TimeUnit.MILLISECONDS);

        return ResponseUtil.successResponse("로그아웃 되었습니다.");
    }

    private void checkRefreshToken(String name, String refreshToken){
        var redisRefreshToken = redisUtil.getData("refreshToken:" + name,String.class)
                .orElseThrow(()-> new ApiException(ExceptionEnum.NOT_FOUND_TOKEN));
        if(!redisRefreshToken.equals(refreshToken)){
            throw new ApiException(ExceptionEnum.CONFLICT_TOKEN);
        }
    }

    private void checkPassword(String rawPassword, String findMemberPassword) {
        if (!passwordEncoder.matches(rawPassword, findMemberPassword)) {
            throw new ApiException(INVALID_PASSWORD);
        }
    }

    private Authentication getAuthentication(String email){
        UserDetails userDetails = customAdminDetailService.loadUserByUsername(email);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        // authenticate 매서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
