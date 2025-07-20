package com.verdict.verdict.global.oAuth.handler;

import com.verdict.verdict.domain.user.UserFacade;
import com.verdict.verdict.domain.user.UserPrincipal;
import com.verdict.verdict.domain.user.UserRepository;
import com.verdict.verdict.domain.user.entity.User;
import com.verdict.verdict.global.entities.UserRole;
import com.verdict.verdict.global.jwt.JwtFacade;
import com.verdict.verdict.global.oAuth.ProviderInfo;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
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
    private final String SIGNUP_URL;
    private final UserRepository userRepository;
    private final JwtFacade jwtFacade;
    private final UserFacade userFacade;

    public OAuth2SuccessHandler(@Value("${url.front}") String BASE_URL,
                                @Value("${url.path.signup}") String SIGNUP_PATH,
                                UserRepository userRepository,
                                JwtFacade jwtFacade,
                                UserFacade userFacade) {
        this.userRepository = userRepository;
        this.jwtFacade = jwtFacade;
        this.userFacade = userFacade;
        this.BASE_URL = BASE_URL;
        this.SIGNUP_URL = BASE_URL +"/"+ SIGNUP_PATH;
//        this.SIGNUP_URL = NGROK_URL + SIGNUP_PATH;
        log.info("success urls {}/login || {}", BASE_URL,SIGNUP_URL);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        User user;
        String identifier;
        UserRole userRole;

        if (authentication.getPrincipal() instanceof UserPrincipal) {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            user = userPrincipal.getUser();
            identifier = user.getIdentifier();
            userRole = user.getUserRole();
        } else if (authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            identifier = oAuth2User.getName(); // This is the identifier from the OAuth2 provider
            log.info("User Identifier from OAuth2User: {}", identifier);

            String providerCode = null;
            if (authentication instanceof OAuth2AuthenticationToken) {
                OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
                providerCode = oauthToken.getAuthorizedClientRegistrationId();
            }

            ProviderInfo providerInfo = ProviderInfo.from(providerCode);

            log.info("Attempting to find user with identifier: {} and provider: {}", identifier, providerInfo);
            // Re-fetch user from DB using identifier and providerInfo
            Optional<User> userOptional = userRepository.findByOAuthInfo(identifier, providerInfo);
            user = userOptional.orElseThrow(() -> new IllegalArgumentException("User not found with identifier: " + identifier + " and provider: " + providerInfo));
            userRole = user.getUserRole();
        } else {
            throw new IllegalStateException("Unexpected principal type: " + authentication.getPrincipal().getClass().getName());
        }

        log.info("User Role: {}", userRole);

        jwtFacade.generateAccessToken(response, user);
        jwtFacade.generateRefreshToken(response, user);
        jwtFacade.setReissuedHeader(response);

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
