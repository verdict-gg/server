package com.verdict.verdict.global.oAuth.handler;

import com.verdict.verdict.domain.user.UserRepository;
import com.verdict.verdict.domain.user.entity.User;
import com.verdict.verdict.global.entities.UserRole;
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
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static com.verdict.verdict.global.entities.UserRole.NOT_REGISTERED;

@Slf4j
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final String BASE_URL;
    private final String NGROK_URL;
    private final String SIGNUP_URL;
    private final UserRepository userRepository;

    public OAuth2SuccessHandler(@Value("${url.front}") String BASE_URL,
                                @Value("${url.ngrok}") String NGROK_URL,
                                @Value("${url.path.signup}") String SIGNUP_PATH,
                                UserRepository userRepository) {
        this.userRepository = userRepository;
        this.BASE_URL = BASE_URL;
        this.NGROK_URL = NGROK_URL;
        this.SIGNUP_URL = BASE_URL +"/"+ SIGNUP_PATH;
//        this.SIGNUP_URL = NGROK_URL + SIGNUP_PATH;
        log.info("success urls {}/login || {}  || {}", BASE_URL,NGROK_URL,SIGNUP_URL);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
//        log.info(" oAuth2User | {} | getAttributes {}",oAuth2User, oAuth2User.getAttributes());

        String identifier = oAuth2User.getName(); // OAuth2User의 이름은 일반적으로 사용자 식별자(예: 이메일, ID 등)로 사용됨
        log.info("User Identifier: {}", identifier);
        String email = oAuth2User.getAttributes().get("email").toString();
        log.info("User Email: {}", email);

        //여기서 임시토큰 생성시키자

        Optional<User> user = userRepository.findByIdentifier(identifier);
//                .orElseThrow(() -> new IllegalArgumentException("User not found with identifier: " + identifier));
        UserRole userRole = user.get().getUserRole();
        log.info("User Role: {}", userRole);
        String redirectUrl = getRedirectUrlByRole(userRole, identifier);
        log.info("Redirecting to URL: {}", redirectUrl);

        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
    private String getRedirectUrlByRole(UserRole userRole, String identifier) {

        if (userRole == NOT_REGISTERED) {
            log.info("NEW user ->> {}",SIGNUP_URL);
            return UriComponentsBuilder.fromUriString(SIGNUP_URL)
//                    .queryParam("userRole",  URLEncoder.encode(userRole, StandardCharsets.UTF_8))
                    .queryParam("identifier",  URLEncoder.encode(identifier, StandardCharsets.UTF_8))
                    .build()
                    .toUriString();
        }
        log.info("Auth user ->> {}",BASE_URL);
        return  UriComponentsBuilder.fromUriString(BASE_URL)
//                .queryParam("role",  URLEncoder.encode(StandardCharsets.UTF_8))
                .queryParam("identifier",  URLEncoder.encode(identifier, StandardCharsets.UTF_8))
                .build()
                .toUriString();
    }
}
