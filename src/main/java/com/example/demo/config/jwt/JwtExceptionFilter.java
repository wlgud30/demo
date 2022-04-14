package com.example.demo.config.jwt;

import com.example.demo.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;

@Slf4j
@Component
public class JwtExceptionFilter extends OncePerRequestFilter {

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
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        LinkedHashMap<String, Object> hashMap = new LinkedHashMap<>();
        hashMap.put("code", exceptionCode.getError().getErrorCode());
        hashMap.put("message", exceptionCode.getError().getMessage());
        hashMap.put("httpStatus",exceptionCode.getError().getStatus().getReasonPhrase());
        hashMap.put("result",0);
        log.info(hashMap.toString());
        JSONObject responseJson = new JSONObject(hashMap);

        response.getWriter().print(responseJson);
    }
}