package com.example.demo.config.security;

import com.example.demo.config.jwt.JwtAuthenticationFilter;
import com.example.demo.config.jwt.JwtEntryPoint;
import com.example.demo.config.jwt.JwtExceptionFilter;
import com.example.demo.util.jwtUtil.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtEntryPoint jwtEntryPoint;
    private final JwtExceptionFilter jwtExceptionFilter;
    private final RedisTemplate<String,String> redisTemplate;
    private final JwtTokenUtil jwtTokenUtil;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .antMatchers("*").permitAll();
        //csrf 설정 비활성화
        http.csrf().disable()
                //exception 핸들링 클래스 추가
                .exceptionHandling()
                //h2-console
                .and()
                .headers()
                .frameOptions()
                .sameOrigin()
                //세션 설정 변경 (Stateless)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(STATELESS)
                //권한설정
                .and()
                .authorizeRequests()
                .antMatchers("/api/join","/api","/auth/**","/swagger*/**","/swagger*","/webjars/**","/v2/**","/exception","/login","/api/reissue").permitAll()
                .antMatchers("/api/user/*","/api/user/*/*","/api/user/*/*/*","/api/user/*/*/*/*").hasRole("USER")
                .anyRequest().permitAll()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtEntryPoint)
                .and()
                .logout().disable() // 6
                .sessionManagement().sessionCreationPolicy(STATELESS)
                .and() // 7
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenUtil, redisTemplate), UsernamePasswordAuthenticationFilter.class);
        // JwtAuthenticationFilter를 UsernamePasswordAuthentictaionFilter 전에 적용시킨다.

        http.addFilterBefore(jwtExceptionFilter, JwtAuthenticationFilter.class);
        //jwt config 적용
    }

    @Override
    public void configure(WebSecurity web){
        web.ignoring()
                .antMatchers("/h2-console/**","/favicon.ico","swagger-ui.html");
    }


}
