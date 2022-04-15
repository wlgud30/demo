package com.example.demo.controller;

import com.example.demo.dto.ResponseDto;
import com.example.demo.dto.TokenDto;
import com.example.demo.dto.member.LoginDto;
import com.example.demo.dto.member.MemberAddRequestDto;
import com.example.demo.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
@RestController
@Slf4j
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    public String home(){
        log.debug("hello!!!");
        return "hello world";
    }

    @GetMapping("/member")
    public ResponseEntity<ResponseDto> getMember(){
        log.debug("member service test");
        return memberService.getMember();
    }

    @PostMapping("/join")
    public ResponseEntity<ResponseDto> addMember(@RequestBody MemberAddRequestDto dto){
        return memberService.addMember(dto);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDto> login(@RequestBody LoginDto dto){
        return memberService.login(dto);
    }

    @PostMapping("/reissue")
    public ResponseEntity<ResponseDto> reissue(@RequestBody TokenDto dto){
        return memberService.reissue(dto);
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseDto> logout(@RequestBody TokenDto dto){
        return memberService.logout(dto);
    }
}
