package com.verdict.verdict.service;

import com.verdict.verdict.dto.oauth2.AbstractOAuth2UserInfo;
import com.verdict.verdict.dto.oauth2.OAuth2UserInfoFactory;
import com.verdict.verdict.dto.oauth2.ProviderInfo;
import com.verdict.verdict.dto.oauth2.UserPrincipal;
import com.verdict.verdict.entity.Role;
import com.verdict.verdict.entity.User;
import com.verdict.verdict.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // OAuth2 PK_id: "users name from user info response"
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();
        log.info("userNameAttributeName {}", userNameAttributeName);
        // OAuth2 loginApi provider : google , naver , lol
        String providerCode = userRequest.getClientRegistration().getRegistrationId();
        log.info("providerCode: {}", providerCode);
        ProviderInfo providerInfo = ProviderInfo.from(providerCode);
        // 위에서 받은 데이터 attributesMap 에다 싹 담아
        log.info("providerInfo {}", providerInfo);
        Map<String, Object> attributes = oAuth2User.getAttributes();
        log.info("attributes {}", attributes);
        // userInfo { provider: google,naver , 식별자 : sub,id }
        AbstractOAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.of(providerInfo, attributes);
        log.info("oAuth2UserInfo {}", oAuth2UserInfo);
        String userIdentifier = oAuth2UserInfo.getUserIdentifier();
        log.info("userIdentifier {}", userIdentifier);
        // entity save
        User user = getUser(userIdentifier, providerInfo);

        // Security Context에 저장할 객체
        return new UserPrincipal(user, attributes, userNameAttributeName);
    }

    private User getUser(String userIdentifier, ProviderInfo providerInfo) {
        Optional<User> optionalUser = userRepository.findByOAuthInfo(userIdentifier, providerInfo);
        if (optionalUser.isEmpty()) {
            User unregisteredUser = User.builder()
                    .identifier(userIdentifier)
                    .role(Role.NOT_REGISTERED)
                    .providerInfo(providerInfo)
                    .build();
            return userRepository.save(unregisteredUser);
        } //select * from user where identifier = 'identifier';
        // insert ignore into user (identifier) values ('identifier') on duplicate key update name=value(identifier)
        return optionalUser.get();
    }
}