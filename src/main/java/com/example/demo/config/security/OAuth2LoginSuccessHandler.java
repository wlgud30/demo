package com.example.demo.config.security;

import com.example.demo.dto.ResponseDto;
import com.example.demo.util.jwtUtil.JwtTokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Transactional
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenUtil jwtTokenUtil;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        var token = jwtTokenUtil.generateToken(authentication);
        response.setStatus(200);
        var result = new ResponseDto("",token);
        response.getWriter().write(objectMapper.writeValueAsString(result));
        response.setContentType("application/json;charset=UTF-8");
    }
}
