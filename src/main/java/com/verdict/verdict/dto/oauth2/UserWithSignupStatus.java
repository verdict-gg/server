package com.verdict.verdict.dto.oauth2;

import com.verdict.verdict.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

@Getter
@AllArgsConstructor
public class UserWithSignupStatus implements OAuth2User {
    private final User user;
    private final boolean isNew;
    private final Map<String, Object> attributes;
    private final Collection<? extends GrantedAuthority> authorities;
    private final String nameAttributeKey;

    @Override
    public String getName() {
        return user.getEmail(); // Return user's email as name
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities; // Return authorities
    }
    @Override
    public Map<String, Object> getAttributes() {
        return attributes; // Return attributes
    }
}