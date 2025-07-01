package com.verdict.verdict.global.service;

import com.verdict.verdict.global.info.OAuth2UserInfo;
import com.verdict.verdict.global.info.OAuth2UserInfoFactory;
import com.verdict.verdict.global.constants.ProviderInfo;
import com.verdict.verdict.global.domain.UserPrincipal;
import com.verdict.verdict.entity.Role;
import com.verdict.verdict.entity.User;
import com.verdict.verdict.global.user.UserRepository;
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

        String userNameAttributeNameFromRequest = request.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        String providerCode = request.getClientRegistration().getRegistrationId();

        ProviderInfo providerInfo = ProviderInfo.from(providerCode);
        log.info("providerInfo:{}",providerCode);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth(providerInfo, attributes);
        log.info("oAuth2UserInfo:{}",oAuth2UserInfo);
        String userIdentifier = oAuth2UserInfo.getUserIdentifier();

        User user = getUser(userIdentifier, providerInfo);

        log.info("OAuth2 로그인 처리. |@|providerInfo={{}}|@|", request);

        //3. 사용자 고유 식별자,이름,메일 추출
        String code = oAuth2UserInfo.getProviderCode(); // code
        String iden = oAuth2UserInfo.getUserIdentifier(); // iden
        log.info("OAuth2 로그인 처리. |@|providerInfo={{}}|@| 고유 ID: {}, 이름: {}, 이메일: {}", code, iden, null, null);

        // Security Context에 저장
        return new UserPrincipal(user, attributes, userNameAttributeNameFromRequest);
    }
    private User getUser(String userIdentifier, ProviderInfo providerInfo) {
        Optional<User> optionalUser = userRepository.findByOAuthInfo(userIdentifier, providerInfo);

        if (optionalUser.isEmpty()) {
            User unregisteredUser = User.builder()
                    .identifier(userIdentifier)
                    .role(Role.NOT_REGISTERED)
                    .providerInfo(providerInfo)
                    .build();
            log.info("NEW USER: ID {}, MAIL {}", unregisteredUser.getIdentifier(), unregisteredUser.getEmail());
            return userRepository.save(unregisteredUser);
        }
        return optionalUser.get();
    }
}