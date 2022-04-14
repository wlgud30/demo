package com.example.demo.member;

import com.example.demo.controller.MemberController;
import com.example.demo.dto.member.MemberAddRequestDto;
import com.example.demo.repository.member.MemberRepository;
import com.example.demo.service.member.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({MemberController.class})
public class MemberAddTests{

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    private MemberAddRequestDto memberAddDto() {
        final MemberAddRequestDto memberAddDto = new MemberAddRequestDto();
        memberAddDto.setEmail("test@test.test");
        memberAddDto.setPassword("1234");
        memberAddDto.setUsername("테스터!");
        return memberAddDto;
    }


    @Test
    @DisplayName("멤버 등록 테스트")
    void memberAddTest() throws Exception {
        final MemberAddRequestDto memberAddDto = memberAddDto();
//        doReturn(false).when(memberRepository).existsByEmail(memberAddDto.getEmail());
        doReturn(memberAddDto).when(memberService).addMember(any(MemberAddRequestDto.class));

        ResultActions actions = mockMvc.perform(post("/api/member")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberAddDto)));

        actions.andDo(print())
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value(-1002));
    }
}
