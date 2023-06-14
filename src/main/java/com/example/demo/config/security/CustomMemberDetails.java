package com.example.demo.config.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


/**
 * redis 역직렬화 에러 때문에 CustomUserDetails 파일 생성
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CustomMemberDetails implements UserDetails {

    private String username;
    private String password;
    private Long userId;
    private boolean isEnabled;
    private boolean isAccountNonExpired;
    private boolean isAccountNonLocked;
    private boolean isCredentialsNonExpired;
    @JsonIgnore
    private Collection<? extends GrantedAuthority> authorities;
    private List<String> roles;

    public CustomMemberDetails(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }
}
