package com.verdict.verdict.handler;

import com.verdict.verdict.dto.oauth2.UserWithSignupStatus;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static java.net.URLEncoder.encode;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    @Value("${front-server.url}")
    private String frontServerUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        // 인증 객체에서 사용자 정보 추출
        UserWithSignupStatus userWithSignupStatus = (UserWithSignupStatus) authentication.getPrincipal();
        OAuth2User delegate = userWithSignupStatus.getDelegate();
        //네이버용
        Map<String, Object> attributes = delegate.getAttributes();
        Map<String, Object> responseMap = (Map<String, Object>) attributes.get("response");


        String email = (String) userWithSignupStatus.getName();
        String name = (String) responseMap.get("name");

        String encodedEmail = encode(email != null ? email : "", StandardCharsets.UTF_8);
        String encodedName = encode(name != null ? name : "", StandardCharsets.UTF_8);


        //구글 로그인이면 OIDC로.
        if (delegate instanceof OidcUser) {
            log.info("구글 OIDC 로그인 : {}", encodedEmail);
            // 구글 전용 추가 로직 작성
        } else {
            log.info("네이버 OAuth2 로그인: {}", encodedEmail);
            // 네이버 전용 추가 로직 작성
        }


        String signupStatus = userWithSignupStatus.getSignupStatus();
        // 타입 안전성 체크

//        if (!(principal instanceof UserWithSignupStatus)) {
//            throw new IllegalStateException("에러임  " + principal.getClass());
//        }


        // 관리자면 메인, 아니면 /signup으로 리다이렉트 나머진 다 유저롤 /

        if ("ROLE_ADMIN".equals(userWithSignupStatus.getName())) {
            response.sendRedirect(frontServerUrl + "/admin");
        }
        // 기존회원이 또 로그인 하려는거
        String redirectUrl;
        if ("NEW".equals(signupStatus)) {
            redirectUrl = String.format("%s/signup?email=%s&name=%s&status=%s",
                    frontServerUrl, encodedEmail, encodedName, signupStatus);
        } else {
            redirectUrl = String.format("%s/signin?email=%s&name=%s",
                    frontServerUrl, encodedEmail, encodedName);
        }
        // 신규/기존 여부에 따라 분기 처리용 쿼리 파라미터 전달
        response.sendRedirect(redirectUrl);
    }
}
