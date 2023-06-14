package com.example.demo.config.security;

import com.example.demo.domain.Authority;
import com.example.demo.enums.ExceptionEnum;
import com.example.demo.exception.ApiException;
import com.example.demo.repository.admin.AdminRepository;
import com.example.demo.repository.authority.AuthorityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomAdminDetailService implements UserDetailsService {

    private final AdminRepository adminRepository;

    private final AuthorityRepository authoritiesRepository;
    @Override
    @Cacheable(value = "Admin", key="#username", cacheManager="cacheManager")
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var admin = adminRepository.findByEmail(username)
                .orElseThrow(()-> new ApiException(ExceptionEnum.NOT_FOUND_ADMIN,"해당하는 유저를 찾을 수 없습니다."));
        return CustomMemberDetails.builder()
                .username(admin.getEmail())
                .userId(admin.getId())
                .isEnabled(true)
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .authorities(admin.getAuthorities())
                .roles(admin.getAuthorities().stream().map(Authority::getAuthority).collect(toList()))
                .build();
    }
}
