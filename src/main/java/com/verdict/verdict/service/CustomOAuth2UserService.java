package com.verdict.verdict.service;

import com.verdict.verdict.dto.oauth2.OAuth2UserInfo;
import com.verdict.verdict.dto.oauth2.UserWithSignupStatus;
import com.verdict.verdict.entity.User;
import com.verdict.verdict.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;
    private final OidcUserService oidcUserService = new OidcUserService();
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        String provider = userRequest.getClientRegistration().getRegistrationId();

        if (userRequest instanceof OidcUserRequest) {
            // 구글(OIDC) 로그인 처리
            return processGoogleUser((OidcUserRequest) userRequest);
        } else if ("naver".equals(provider)) {
            // 네이버(OAuth2) 로그인 처리
            return processNaverUser(userRequest);
        } else {
            // 기타 OAuth2 공급자 처리
            return super.loadUser(userRequest);
        }
    }

    // 구글(OIDC) 로그인 처리
    private OAuth2User processGoogleUser(OidcUserRequest userRequest) {
        OidcUser oidcUser = oidcUserService.loadUser(userRequest);
        log.info("Google OIDC User: {}", oidcUser.getAttributes());

        // 구글 전용 파싱 및 DB 처리
        OAuth2UserInfo userInfo = OAuth2UserInfo.of("google", oidcUser.getAttributes());
        return createOrUpdateUserAndReturn(userInfo, oidcUser);
    }

    // 네이버(OAuth2) 로그인 처리
    private OAuth2User processNaverUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("Naver OAuth2 User: {}", oAuth2User.getAttributes());

        OAuth2UserInfo userInfo = OAuth2UserInfo.of("naver", oAuth2User.getAttributes());
        return createOrUpdateUserAndReturn(userInfo, oAuth2User);
    }

    // 공통 DB 처리 및 UserWithSignupStatus 반환
    private OAuth2User createOrUpdateUserAndReturn(OAuth2UserInfo userInfo, OAuth2User oAuth2User) {
        Optional<User> userOptional = userRepository.findByProviderAndProviderId(
                userInfo.getProvider(), userInfo.getProviderId()
        );
        boolean isNew = userOptional.isEmpty();

        User user = userOptional.map(existing -> updateIfChanged(existing, userInfo))
                .orElseGet(userInfo::toEntity);
        if (isNew) {
            userRepository.save(user);
        }
        return new UserWithSignupStatus(user, oAuth2User, isNew ? "NEW" : "EXISTING");
    }
    private User updateIfChanged(User user, OAuth2UserInfo userInfo) {
        boolean changed = false;
        // 프사 changed
        if (!Objects.equals(user.getImageUrl(), userInfo.getImageUrl())) {
            user.setImageUrl(userInfo.getImageUrl());
            changed = true;
        }
        if (changed) {
            userRepository.save(user);
        }
        return user;
    }
}