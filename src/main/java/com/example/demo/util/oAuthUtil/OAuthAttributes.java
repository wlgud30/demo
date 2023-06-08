package com.example.demo.util.oAuthUtil;

import com.example.demo.domain.Authority;
import com.example.demo.domain.Member;
import com.example.demo.enums.ExceptionEnum;
import com.example.demo.enums.LoginProvider;
import com.example.demo.exception.ApiException;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class OAuthAttributes {
    private Map<String,Object> attributes;
    private String nameAttributeKey;
    private String email;
    private String name;
    private LoginProvider provider;
    private String providerId;

    public static OAuthAttributes of(String registrationId,String userNameAttributeName,Map<String,Object> attributes){
        if("naver".equals(registrationId)){
            return ofNaver("id",attributes);
        }
        if("kakao".equals(registrationId)){
            return ofKakao("id",attributes);
        }
        if("google".equals(registrationId)){
            return ofGoogle(userNameAttributeName,attributes);
        }
        if ("apple".equals(registrationId)) {
            return ofApple(userNameAttributeName, attributes);
        }
        throw new ApiException(ExceptionEnum.BAD_REQUEST,"지원하지 않는 로그인 방식입니다.");
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName,Map<String,Object> attributes){
        return OAuthAttributes.builder()
                .name((String)attributes.get("name"))
                .email((String)attributes.get("email"))
                .provider(LoginProvider.GOOGLE)
                .providerId((String)attributes.get("sub"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    private static OAuthAttributes ofNaver(String userNameAttributeName,Map<String,Object> attributes){
        Map<String,Object> response = (Map<String,Object>)attributes.get("response");
        return OAuthAttributes.builder()
                .name((String)response.get("name"))
                .email((String)response.get("email"))
                .provider(LoginProvider.NAVER)
                .providerId((String)response.get("id"))
                .attributes(response)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName,Map<String,Object> attributes){
        Map<String,Object> kakaoAccount = (Map<String,Object>)attributes.get("kakao_account");
        return OAuthAttributes.builder()
                .name((String)kakaoAccount.get("nickname"))
                .email((String)kakaoAccount.get("email"))
                .provider(LoginProvider.KAKAO)
                .attributes(kakaoAccount)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    private static OAuthAttributes ofApple(String userNameAttributeName,Map<String,Object> attributes){
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email" ))
                .provider(LoginProvider.APPLE)
                .providerId((String) attributes.get("sub"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public Member toEntity(){
        Member member = Member.builder()
                .email(email)
                .username(name)
                .provider(provider)
                .providerId(providerId)
                .build();
        member.addAuthority(Authority.ofUser(member));
        return member;
    }
}
