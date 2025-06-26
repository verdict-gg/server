package com.verdict.verdict.service;

import com.verdict.verdict.dto.oauth2.OAuth2UserInfo;
import com.verdict.verdict.entity.User;
import com.verdict.verdict.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;


import java.util.Collections;
import java.util.Set;


@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {


    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        OAuth2UserInfo userInfo = OAuth2UserInfo.of(registrationId, oAuth2User.getAttributes());

        User user = saveOrUpdate(userInfo);

        Set<GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(user.getRole().name()));
        log.info("{} {} {}", authorities,  registrationId, user.getEmail());

        String nameAttributeKey = registrationId.equals("google") ? "sub" : registrationId.equals("naver")? "response": "id";

        return new DefaultOAuth2User(
                authorities, // 권한
                oAuth2User.getAttributes(),
                nameAttributeKey);
    }

    // 사용자 정보를 저장or 업데이트
    private User saveOrUpdate(OAuth2UserInfo userInfo) {
        User user = userRepository.findByProviderAndProviderId(userInfo.getProvider(), userInfo.getProviderId())
                .orElseGet(userInfo::toEntity);
        userRepository.save(user);
        return user;
    }
}
