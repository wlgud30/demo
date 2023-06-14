package com.example.demo.controller;

import com.example.demo.dto.ResponseDto;
import com.example.demo.service.member.MemberService;
import com.example.demo.vo.MemberInfoVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "MemberController",description = "회원 관련 API")
public class MemberController {

    private final MemberService memberService;

//    @GetMapping
//    public String home(){
//        log.debug("hello!!!");
//        return "home";
//    }

    @Operation(summary = "회원 조회", description = "로그인한 회원을 조회합니다.",tags = {"MemberController"})
//    @ApiResponses({
//    @ApiResponse(responseCode = "401",description = "Unauthorized",content = @Content(schema = @Schema(implementation = ResponseDto.class))),
//    @ApiResponse(responseCode = "200",description = "OK",content = @Content(schema = @Schema(implementation = MemberInfoVO.class)))
//    })
    @GetMapping("/member")
    public ResponseEntity<ResponseDto<MemberInfoVO>> getMember(){
        log.debug("member service test");
        return memberService.getMember();
    }

//    @PostMapping("/join")
//    public ResponseEntity<ResponseDto> addMember(@RequestBody MemberAddRequestDto dto){
//        return memberService.addMember(dto);
//    }

//    @PostMapping("/login")
//    public ResponseEntity<ResponseDto> login(@RequestBody LoginDto dto){
//        return memberService.login(dto);
//    }

//    @PostMapping("/reissue")
//    public ResponseEntity<ResponseDto> reissue(@RequestBody TokenDto dto){
//        return memberService.reissue(dto);
//    }

//    @PostMapping("/logout")
//    public ResponseEntity<ResponseDto> logout(@RequestBody TokenDto dto){
//        return memberService.logout(dto);
//    }
}
