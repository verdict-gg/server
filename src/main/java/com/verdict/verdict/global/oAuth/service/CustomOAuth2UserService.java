package com.verdict.verdict.global.oAuth.service;

import com.verdict.verdict.domain.user.UserPrincipal;
import com.verdict.verdict.domain.user.UserRepository;
import com.verdict.verdict.domain.user.entity.User;
import com.verdict.verdict.global.entities.UserRole;
import com.verdict.verdict.global.oAuth.ProviderInfo;
import com.verdict.verdict.global.oAuth.info.OAuth2UserInfo;
import com.verdict.verdict.global.oAuth.info.OAuth2UserInfoFactory;
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
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(request);

        String providerCode = request.getClientRegistration().getRegistrationId();
        log.info("providerCode: {}", providerCode);
        String userNameAttributeNameFromRequest = request.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();
        log.info("userNameAttributeNameFromRequest: {}", userNameAttributeNameFromRequest);

        ProviderInfo providerInfo = ProviderInfo.from(providerCode);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth(providerInfo, attributes);
        String userIdentifier = oAuth2UserInfo.getUserIdentifier();
        log.info("OAuth2 ||| 로그인요청 code: {}, identifier: {}", providerInfo, oAuth2UserInfo.getUserIdentifier());
        User user = getUser(userIdentifier, providerInfo);
        log.info("user DB검증 ||| providerInfo: {}, auth: {}", userIdentifier, user.getUserRole());
        log.info("USER 권 한 ||| {}", user.getUserRole());

        // Security Context에 저장
        return new UserPrincipal(user, attributes, userNameAttributeNameFromRequest);
    }

    private User getUser(String userIdentifier, ProviderInfo providerInfo) {
        Optional<User> optionalUser = userRepository.findByOAuthInfo(userIdentifier, providerInfo);

        if (optionalUser.isEmpty()) {
            User unregisteredUser = User.builder()
                    .identifier(userIdentifier)
                    .userRole(UserRole.NOT_REGISTERED)
                    .providerInfo(providerInfo)
                    .build();
            log.info("NEW USER: ID {}, MAIL {}", unregisteredUser.getIdentifier(), unregisteredUser.getEmail());
            return userRepository.save(unregisteredUser);
        }
        return optionalUser.get();
    }
}