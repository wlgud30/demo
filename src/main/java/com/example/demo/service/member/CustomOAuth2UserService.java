package com.example.demo.service.member;

import com.example.demo.repository.member.MemberRepository;
import com.example.demo.util.oAuthUtil.OAuthAttributes;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest,OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        var registrationId = userRequest.getClientRegistration().getRegistrationId();
        var userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();
        var attributes = OAuthAttributes.of(registrationId,userNameAttributeName,oAuth2User.getAttributes());

        save(attributes);

        return new DefaultOAuth2User(
                oAuth2User.getAuthorities(),
                attributes.getAttributes(),
                attributes.getNameAttributeKey()
        );
    }

    private void save(OAuthAttributes attributes){
        if(memberRepository.findByEmailAndProvider(attributes.getEmail(),attributes.getProvider()).isEmpty()){
            memberRepository.save(attributes.toEntity());
        }
    }
}
