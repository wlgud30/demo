package com.example.demo.config.jwt;

import com.example.demo.dto.ResponseDto;
import com.example.demo.exception.ApiException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtExceptionFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {
        try {
            chain.doFilter(req, res); // go to 'JwtAuthenticationFilter'
        } catch (ApiException e) {
            setResponse(res, e);
        }
    }

    private void setResponse(HttpServletResponse response, ApiException exceptionCode) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(401);
        var result = ResponseDto.<Void>failApiBuilder().e(exceptionCode.getError()).build();
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}