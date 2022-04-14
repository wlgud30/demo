package com.example.demo.dto.member;

import com.example.demo.domain.Authority;
import com.example.demo.domain.Member;
import lombok.Data;

import java.util.UUID;

@Data
public class MemberAddRequestDto {

    private String email;

    private String password;

    private String username;

    public Member toUser() {
        Member member = Member.builder()
                .username(UUID.randomUUID().toString())
                .email(email)
                .password(password)
                .username(username)
                .build();
        member.addAuthority(Authority.ofUser(member));
        return member;
    }

    public Member toAdmin() {
        Member member = Member.builder()
                .username(UUID.randomUUID().toString())
                .email(email)
                .password(password)
                .username(username)
                .build();
        member.addAuthority(Authority.ofAdmin(member));
        return member;
    }



}
