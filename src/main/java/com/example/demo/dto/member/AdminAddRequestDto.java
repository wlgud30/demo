package com.example.demo.dto.member;

import com.example.demo.domain.Admin;
import com.example.demo.domain.Authority;
import lombok.Data;

import java.util.UUID;

@Data
public class AdminAddRequestDto {

    private String email;

    private String password;
    private String username;

    public Admin toAdmin() {
        Admin admin = Admin.builder()
                .username(UUID.randomUUID().toString())
                .email(email)
                .password(password)
                .username(username)
                .build();
        admin.addAuthority(Authority.ofAdmin(admin));
        return admin;
    }


}
