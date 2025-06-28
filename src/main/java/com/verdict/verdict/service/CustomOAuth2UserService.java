package com.verdict.verdict.service;

import com.verdict.verdict.dto.oauth2.OAuth2UserInfo;
import com.verdict.verdict.dto.oauth2.UserWithSignupStatus;
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

import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // google | naver
        String providerId = userRequest.getClientRegistration().getRegistrationId();

        // provider , user info
        OAuth2UserInfo userInfo = OAuth2UserInfo.of(providerId, oAuth2User);

        // save
        User user = saveOrUpdate(userInfo, providerId);

        return new UserWithSignupStatus(user, userInfo.getAttributes());
    }


    private User saveOrUpdate(OAuth2UserInfo userInfo, String providerId) {
        Optional<User> optionalUser = userRepository.findByIdentifier(userInfo.getEmail());
        User user;
        boolean changed = false;
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
            //프사 변경만
            if (!user.getImageUrl().equals(userInfo.getImageUrl())) {
                user.setImageUrl(userInfo.getImageUrl());
            }
        } else {
            // NEWUSER
            user = User.builder().
                    email(userInfo.getEmail())
                    .imageUrl(userInfo.getImageUrl())
                    .provider("여기에 제공사? 골햄과상의")
                    .providerId(providerId)
                    .role(Role.ROLE_USER)
                    .build();
            changed = true;
        }
        return !changed ? user : userRepository.save(user);
    }
}