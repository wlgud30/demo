package com.example.demo.config.jwt;

import com.example.demo.enums.ExceptionEnum;
import com.example.demo.exception.ApiException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.error("Unauthorized error : {}",authException.getMessage());
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Error : Unauthorized");
        var result = new ApiException(ExceptionEnum.LOGIN_REQUIRED);
        response.getWriter().write(objectMapper.writeValueAsString(result));
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
    }
}
