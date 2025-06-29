package com.verdict.verdict.handler;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
@Slf4j
@Component
public class OAuth2FailureHandler extends SimpleUrlAuthenticationFailureHandler {


    private final String REDIRECT_URL;
    private final String ERROR_PARAM_PREFIX = "error";

    public OAuth2FailureHandler(@Value("${url.base}") String REDIRECT_URL){
        log.info("Fail url {}", REDIRECT_URL);
        this.REDIRECT_URL = REDIRECT_URL;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String redirectUrl = UriComponentsBuilder.fromUriString(REDIRECT_URL)
                .queryParam(ERROR_PARAM_PREFIX, exception.getLocalizedMessage())
                .build()
                .toUriString();
        // 시큐리티가 프론트로 토스할 url 커스터마이징., query string 에러메세지 추가.
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}