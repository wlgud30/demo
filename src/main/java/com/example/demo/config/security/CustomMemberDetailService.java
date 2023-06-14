package com.example.demo.config.security;

import com.example.demo.domain.Authority;
import com.example.demo.domain.Member;
import com.example.demo.enums.ExceptionEnum;
import com.example.demo.exception.ApiException;
import com.example.demo.repository.authority.AuthorityRepository;
import com.example.demo.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

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
    @Cacheable(value = "Member", key="#username", cacheManager="cacheManager")
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info(username);
        Member member = memberRepository.findByProviderId(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다."));
        log.info(member.toString());
        return CustomMemberDetails.builder()
                .username(member.getEmail())
                .userId(member.getId())
                .isEnabled(true)
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .authorities(member.getAuthorities())
                .roles(member.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(toList()))
                .build();
    }

    private Collection<GrantedAuthority> getAuthorities(Long userId) {
        List<Authority> authList = authoritiesRepository.findByUserId(userId)
                .orElseThrow(()-> new ApiException(ExceptionEnum.BAD_REQUEST,"권한 정보를 찾을 수 없습니다."));
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Authority authority : authList) {
            authorities.add(new SimpleGrantedAuthority(authority.getAuthority()));
        }
        return authorities;
    }

}
