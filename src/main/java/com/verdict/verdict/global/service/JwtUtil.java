package com.verdict.verdict.global.service;


import com.verdict.verdict.global.constants.JwtRule;
import com.verdict.verdict.global.constants.TokenStatus;
import com.verdict.verdict.global.util.BusinessException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Base64;

import static com.verdict.verdict.global.util.ErrorCode.*;

@Slf4j
@Component
class JwtUtil {

    public TokenStatus getTokenStatus(String token, Key secretKey) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return TokenStatus.AUTHENTICATED;
        } catch (ExpiredJwtException e) {
            log.error(INVALID_EXPIRED_JWT.getMessage());
            return TokenStatus.EXPIRED;
        } catch (JwtException e) {
            log.error("JWT 유효성 검증 실패: {}", e.getMessage(), e);
            throw new BusinessException(INVALID_JWT); // BusinessException으로 감싸서 던짐
        }
    }

    public String resolveTokenFromCookie(Cookie[] cookies, JwtRule tokenPrefix) {
        if (cookies == null) {
            throw new BusinessException(JWT_NOT_FOUND_IN_COOKIE);
        }
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(tokenPrefix.getValue()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new BusinessException(JWT_NOT_FOUND_IN_COOKIE));
    }

    public Key getSigningKey(String base64SecretKey) {
        byte[] keyBytes = Base64.getDecoder().decode(base64SecretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Cookie resetCookie(JwtRule tokenPrefix) {
        Cookie cookie = new Cookie(tokenPrefix.getValue(), null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
//        cookie.setHttpOnly(true); // 배포시 true로
//        cookie.setSecure(true); // 배포시 true로
//        cookie.setDomain("verdict.gg");
        cookie.setDomain("localhost");
        return cookie;
    }
}