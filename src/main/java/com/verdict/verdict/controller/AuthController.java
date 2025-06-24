package com.verdict.verdict.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {



    @GetMapping("/google")
    public ResponseEntity<String> googleAuth(@RegisteredOAuth2AuthorizedClient("google") OAuth2AuthorizedClient authorizedClient, OAuth2AuthenticationToken authentication) {
        OAuth2User user = authentication.getPrincipal();
        log.info("Google user: {}", user.getAttributes());
        log.info("Access Token: {}", authorizedClient.getAccessToken().getTokenValue());
        return ResponseEntity.ok("google endpoint test");

    }

    @GetMapping("/naver")
    public ResponseEntity<String> naverAuth(@RegisteredOAuth2AuthorizedClient("naver") OAuth2AuthorizedClient authorizedClient, OAuth2AuthenticationToken authentication) {
        OAuth2User user = authentication.getPrincipal();
        log.info("Naver user: {}", user.getAttributes());
        log.info("Access Token: {}", authorizedClient.getAccessToken().getTokenValue());
        return ResponseEntity.ok("naver endpoint test");
    }
}