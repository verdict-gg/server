package com.verdict.verdict.global.handler;

import com.verdict.verdict.entity.Role;
import com.verdict.verdict.entity.User;
import com.verdict.verdict.global.domain.UserPrincipal;
import com.verdict.verdict.global.user.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final String BASE_URL;
    private final String NGROK_URL;
    private final String SIGNUP_URL;
    private final UserRepository userRepository;

    public OAuth2SuccessHandler(@Value("${url.base}") String BASE_URL,
                                @Value("${url.ngrok}") String NGROK_URL,
                                @Value("${url.path.signup}") String SIGNUP_PATH,
                                UserRepository userRepository) {
        this.userRepository = userRepository;
        this.BASE_URL = BASE_URL;
        this.NGROK_URL = NGROK_URL;
        this.SIGNUP_URL = NGROK_URL + SIGNUP_PATH;
        log.info("success urls {}/login || {}  || {}", BASE_URL,NGROK_URL,SIGNUP_URL);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // Http request 온거 인증은 클라 라이브러리가 알아서 하고 난 identifier에 담아서 엔티티 조회&검사
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        User user = userPrincipal.getUser();

        log.info("제대로 들옴 ? {}",user);
        String identifier = userPrincipal.getName();
        String email = user.getEmail();
        log.info("1. iden | {} | 2.mail {}",identifier,email);


        String redirectUrl = getRedirectUrlByRole(user.getRole(), identifier, email);

//        User user = userRepository.findByIdentifier(identifier)
//                .orElseThrow(
//                        () -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

//        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
//        String identifier = oAuth2User.getName();

        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
    private String getRedirectUrlByRole(Role role, String identifier,String email) {

        if (role == Role.NOT_REGISTERED) {
            log.info("NEW user ->> {}",SIGNUP_URL);
            return UriComponentsBuilder.fromUriString(SIGNUP_URL)
                    .queryParam("identifier",  URLEncoder.encode(identifier, StandardCharsets.UTF_8))
                    .queryParam("email",  URLEncoder.encode(email, StandardCharsets.UTF_8))
                    .build()
                    .toUriString();
        }
        log.info("Auth user ->> {}",BASE_URL);
        return  UriComponentsBuilder.fromUriString(BASE_URL)
                .queryParam("identifier",  URLEncoder.encode(identifier, StandardCharsets.UTF_8))
                .queryParam("email",  URLEncoder.encode(email, StandardCharsets.UTF_8))
                .build()
                .toUriString();
    }
}
