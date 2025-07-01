package com.verdict.verdict.global.service;

import com.verdict.verdict.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;

public interface JwtFacade {
    String generateAccessToken(HttpServletResponse response, User user);

    String generateRefreshToken(HttpServletResponse response, User user);

    String resolveAccessToken(HttpServletRequest request);

    String resolveRefreshToken(HttpServletRequest request);

    String getIdentifierFromRefresh(String refreshToken);

    boolean validateAccessToken(String accessToken);

    boolean validateRefreshToken(String refreshToken, String identifier);

    void setReissuedHeader(HttpServletResponse response);

    void logout(HttpServletResponse response, String identifier);

    Authentication getAuthentication(String accessToken);
}