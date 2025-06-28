package com.verdict.verdict.handler;

import com.verdict.verdict.entity.Role;
import com.verdict.verdict.entity.User;
import com.verdict.verdict.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    //    @Value("${front-server.url}")
//    private final String frontServerUrl;
    private final String SIGNUP_URL;
    private final String AUTH_URL;
    private final UserRepository userRepository;

    public OAuth2SuccessHandler(@Value("${url.base}") String BASE_URL,
                                @Value("${url.path.signup}") String SIGNUP_PATH,
                                @Value("${url.base.auth}") String AUTH_PATH,
                                UserRepository userRepository) {
        this.userRepository = userRepository;
        this.SIGNUP_URL = BASE_URL + SIGNUP_PATH;
        this.AUTH_URL = BASE_URL + AUTH_PATH;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 인증 객체에서 사용자 정보 추출
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String identifier = "dsa";
        String providerId = oAuth2User.getName(); //  User 엔티티의 식별자 값 갖다 쓰자

        Optional<User> uesr = userRepository.findByIdentifier(identifier);
        String redirectUrl = getRedirectUrlByRole(userRepository.findAllByIdentifier(), providerId);
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);

    }

    private String getRedirectUrlByRole(Role role, String identifier) {
        if (role == Role.NOT_REGISTERED) {
            return UriComponentsBuilder.fromUriString(SIGNUP_URL).queryParam("identifier", identifier)
                    .build().toUriString();
        }
        return  UriComponentsBuilder.fromUriString(AUTH_URL).queryParam("identifier", identifier).build().toUriString();

    }
}
