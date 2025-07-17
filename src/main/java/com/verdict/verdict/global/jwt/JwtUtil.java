package com.verdict.verdict.global.jwt;


import com.verdict.verdict.global.exception.BusinessException;
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

import static com.verdict.verdict.global.exception.ErrorCode.*;

@Slf4j
@Component
class JwtUtil {

    // 문자열 시크릿을 Key 객체로 변환
    public Key getSigningKey(String base64SecretKey) {
        byte[] keyBytes = Base64.getDecoder().decode(base64SecretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // 토큰 상태 조회
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

    // 쿠키 배열에서 특정 이름의 쿠키 값(토큰)을 찾아 반환
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


    //쿠키 초기화(로그아웃 시 만료시켜 덮어씀)
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