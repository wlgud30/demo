package com.example.demo.config.security;

import com.example.demo.domain.Authority;
import com.example.demo.domain.Member;
import com.example.demo.enums.ExceptionEnum;
import com.example.demo.exception.ApiException;
import com.example.demo.repository.authority.AuthorityRepository;
import com.example.demo.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomMemberDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    private final AuthorityRepository authoritiesRepository;

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        return memberRepository.findByEmail(username)
//                .map(this::createUserDetails)
//                .orElseThrow(() -> new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다."));
//    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info(username + "여기 맞냐?");
        Member member = memberRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다."));
        log.info(member.toString());
        return CustomMemberDetails.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .userId(member.getId())
                .isEnabled(true)
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .authorities(member.getAuthorities())
                .build();
    }

    // 해당하는 User 의 데이터가 존재한다면 UserDetails 객체로 만들어서 리턴
//    private UserDetails createUserDetails(Member users) {
//        return new User(users.getEmail(), users.getPassword(), users.getAuthorities());
//    }

    private Collection<GrantedAuthority> getAuthorities(Long user_id) {
        List<Authority> authList = authoritiesRepository.findByUserId(user_id)
                .orElseThrow(()-> new ApiException(ExceptionEnum.BAD_REQUEST,"권한 정보를 찾을 수 없습니다."));
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Authority authority : authList) {
            authorities.add(new SimpleGrantedAuthority(authority.getAuthority()));
        }
        return authorities;
    }

}
