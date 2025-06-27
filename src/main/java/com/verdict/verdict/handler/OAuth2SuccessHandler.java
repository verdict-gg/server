package com.verdict.verdict.handler;


import com.verdict.verdict.dto.oauth2.UserWithSignupStatus;
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
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    @Value("${front-server.url}")
    private String frontServerUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        UserWithSignupStatus user = (UserWithSignupStatus) authentication.getPrincipal();
        String email = URLEncoder.encode(user.getUser().getEmail(), StandardCharsets.UTF_8);
        String name = URLEncoder.encode(user.getName(), StandardCharsets.UTF_8);

        if (user.isNew()) {
            log.info("신규! 이멜,네임 뿌림!: {} {}", user.getUser().getEmail(), user.getName());
            response.sendRedirect(frontServerUrl + "/signup?email=" + email + "&name=" + name);
        } else {
            response.sendRedirect(frontServerUrl + "/?email=" + email + "&name=" + name);
        }
    }
}