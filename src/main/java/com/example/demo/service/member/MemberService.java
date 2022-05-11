package com.example.demo.service.member;

import com.example.demo.config.security.CustomMemberDetailService;
import com.example.demo.config.security.SecurityUtil;
import com.example.demo.domain.Member;
import com.example.demo.dto.ResponseDto;
import com.example.demo.dto.TokenDto;
import com.example.demo.dto.member.LoginDto;
import com.example.demo.dto.member.MemberAddRequestDto;
import com.example.demo.enums.ExceptionEnum;
import com.example.demo.exception.ApiException;
import com.example.demo.repository.member.MemberQueryDslRepository;
import com.example.demo.repository.member.MemberRepository;
import com.example.demo.util.jwtUtil.JwtTokenUtil;
import com.example.demo.util.responseUtil.ResponseUtil;
import com.example.demo.vo.MemberInfoVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final ResponseUtil responseUtil;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RedisTemplate<String, String> redisTemplate;
    private final AuthenticationManager authenticationManager;
    private final CustomMemberDetailService customMemberDetailService;
    private final MemberQueryDslRepository memberQueryDslRepository;

    public ResponseEntity<ResponseDto> getMember(){

        log.info("security util get user email : "+SecurityUtil.getCurrentUser());
        MemberInfoVO memberInfoVO = memberQueryDslRepository.getUserInfo(SecurityUtil.getCurrentUser().getUserId())
                .orElseThrow(RuntimeException::new);
        return responseUtil.successResponse("사용자를 조회합니다.",memberInfoVO);
    }

    @Transactional
    public ResponseEntity<ResponseDto> addMember(MemberAddRequestDto dto){
        if(memberRepository.existsByEmail(dto.getEmail())){
            return responseUtil.successResponse("중복된 이메일이 존재합니다.",0);
        }
        return responseUtil.successResponse("회원가입이 완료 되었습니다.",memberInsert(dto));
    }

    @Transactional
    private Long memberInsert(MemberAddRequestDto dto){
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        return memberRepository.save(dto.toUser()).getId();
    }

    @Transactional
    public ResponseEntity<ResponseDto> login(LoginDto loginDto) {

        Member member = memberRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new ApiException(ExceptionEnum.BAD_REQUEST,"이메일을 확인해주세요."));

        checkPassword(loginDto.getPassword(),member.getPassword());

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
        redisTemplate.opsForValue()
                .set("refreshToken:" + authentication.getName(), tokenInfo.getRefreshToken(), tokenInfo.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);

        return responseUtil.successResponse("로그인에 성공했습니다.",tokenInfo);
    }

    private Authentication getAuthentication(String email){
        UserDetails userDetails = customMemberDetailService.loadUserByUsername(email);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        // authenticate 매서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행
        return SecurityContextHolder.getContext().getAuthentication();
    }

    private void checkPassword(String rawPassword, String findMemberPassword) {
        if (!passwordEncoder.matches(rawPassword, findMemberPassword)) {
            throw new ApiException(ExceptionEnum.BAD_REQUEST,"비밀번호가 맞지 않습니다.");
        }
    }

    @Transactional
    public ResponseEntity<ResponseDto> reissue(TokenDto dto) {

        // 1. Refresh Token 검증
        if (!jwtTokenUtil.validateToken(dto.getRefreshToken())) {
            return responseUtil.tokenVerificationFail("토큰이 유효하지 않습니다.");
        }

        // 2. Access Token 에서 User email 를 가져옵니다.
        Authentication authentication = jwtTokenUtil.getAuthentication(dto.getAccessToken());

        // 3. Redis 에서 User email 을 기반으로 저장된 Refresh Token 값을 가져옵니다.
        String refreshToken = redisTemplate.opsForValue().get("refreshToken:" + authentication.getName());
        try{
            if(!Objects.requireNonNull(refreshToken).equals(dto.getRefreshToken())) {
                throw new ApiException(ExceptionEnum.SIGNATURE_TOKEN);
            }
        } catch (NullPointerException e){
            throw new ApiException(ExceptionEnum.NOT_FOUND_TOKEN);
        }

        // 4. 새로운 토큰 생성
        TokenDto tokenInfo = jwtTokenUtil.generateToken(authentication);

        // 5. RefreshToken Redis 업데이트
        redisTemplate.opsForValue()
                .set("refreshToken:" + authentication.getName(), tokenInfo.getRefreshToken(), tokenInfo.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);

        return responseUtil.successResponse("토큰을 갱신합니다.",tokenInfo);
    }

    public ResponseEntity<ResponseDto> logout(TokenDto logout) {
        // 1. Access Token 검증
        if (!jwtTokenUtil.validateToken(logout.getAccessToken())) {
            return responseUtil.badRequestResponse("잘못된 요청입니다.");
        }

        // 2. Access Token 에서 User email 을 가져옵니다.
        Authentication authentication = jwtTokenUtil.getAuthentication(logout.getAccessToken());

        // 3. Redis 에서 해당 User email 로 저장된 Refresh Token 이 있는지 여부를 확인 후 있을 경우 삭제합니다.
        if (redisTemplate.opsForValue().get("refreshToken:" + authentication.getName()) != null) {
            // Refresh Token 삭제
            redisTemplate.delete("refreshToken:" + authentication.getName());
        }

        // 4. 해당 Access Token 유효시간 가지고 와서 BlackList 로 저장하기
        Long expiration = jwtTokenUtil.getExpiration(logout.getAccessToken());
        redisTemplate.opsForValue()
                .set(logout.getAccessToken(), "logout", expiration, TimeUnit.MILLISECONDS);

        return responseUtil.successResponse("로그아웃 되었습니다.");
    }


}
