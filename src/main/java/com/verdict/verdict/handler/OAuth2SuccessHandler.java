package com.verdict.verdict.handler;


import com.verdict.verdict.dto.oauth2.UserWithSignupStatus;
import com.verdict.verdict.service.CustomOAuth2UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    @Value("${front-server.url}")
    private String frontServerUrl = "https://verdict-gg.vercel.app";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        UserWithSignupStatus user = (UserWithSignupStatus) authentication.getPrincipal();

        if (user.isNew()) {
            log.info("신규! 이메일주소만 뿌림!: {}", user.getUser().getEmail());
            String email = URLEncoder.encode(user.getUser().getEmail(), "UTF-8");
            response.sendRedirect(frontServerUrl + "/signup?email=" + email);
        } else {
            response.sendRedirect(frontServerUrl); //기존유저는 메인으로.
        }
    }
}