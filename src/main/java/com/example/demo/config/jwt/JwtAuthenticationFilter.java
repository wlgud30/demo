package com.example.demo.config.jwt;

import com.example.demo.config.security.CustomMemberDetailService;
import com.example.demo.enums.ExceptionEnum;
import com.example.demo.exception.ApiException;
import com.example.demo.util.jwtUtil.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final CustomMemberDetailService customMemberDetailService;

    private String getToken(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//
//        // 1. Request Header 에서 JWT 토큰 추출
//        String token = getToken((HttpServletRequest) request);
//
//        // 2. validateToken 으로 토큰 유효성 검사
//        try {
//            if (token != null && jwtTokenUtil.validateToken(token)) {
//                // (추가) Redis 에 해당 accessToken logout 여부 확인
//                String isLogout = (String) redisTemplate.opsForValue().get(token);
//                if (ObjectUtils.isEmpty(isLogout)) {
//                    // 토큰이 유효할 경우 토큰에서 Authentication 객체를 가지고 와서 SecurityContext 에 저장
//                    String username = jwtTokenUtil.getUsername(token);
//                    UserDetails userDetails = customMemberDetailService.loadUserByUsername(username);
//                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());
//                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                    Authentication authentication = jwtTokenUtil.getAuthentication(token);
//                    SecurityContextHolder.getContext().setAuthentication(authentication);
//                }
//            }
//        } catch (SignatureException e){
//            log.error("토큰이 유효하지 않습니다.",e);
//            throw new ApiException(ExceptionEnum.SIGNATURE_TOKEN);
//        }catch (IllegalArgumentException e) {
//            log.error("토큰이 유효하지 않습니다.", e);
//            throw new ApiException(ExceptionEnum.INVALID_TOKEN);
//        } catch (ExpiredJwtException e) {
//            log.error("토큰이 만료 되었습니다.", e);
//            throw new ApiException(ExceptionEnum.EXPIRED_JWT_EXCEPTION);
//        }
//        chain.doFilter(request, response);
//    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = getToken(request);

        // 2. validateToken 으로 토큰 유효성 검사
        try {
            if (token != null && jwtTokenUtil.validateToken(token)) {
                // 토큰이 유효할 경우 토큰에서 Authentication 객체를 가지고 와서 SecurityContext 에 저장
                String username = jwtTokenUtil.getUsername(token);
                log.info("JWT authentication filter : "+username);
                UserDetails userDetails = customMemberDetailService.loadUserByUsername(username);
                processSecurity(request,userDetails);
            }
        } catch (SignatureException e){
            log.error("토큰이 유효하지 않습니다.",e);
            throw new ApiException(ExceptionEnum.SIGNATURE_TOKEN);
        }catch (IllegalArgumentException e) {
            log.error("토큰이 유효하지 않습니다.", e);
            throw new ApiException(ExceptionEnum.INVALID_TOKEN);
        } catch (ExpiredJwtException e) {
            log.error("토큰이 만료 되었습니다.", e);
            throw new ApiException(ExceptionEnum.EXPIRED_JWT_EXCEPTION);
        }
        filterChain.doFilter(request, response);
    }

    private void processSecurity(HttpServletRequest request, UserDetails userDetails) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());
        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }
}
