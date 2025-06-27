package com.verdict.verdict.dto.oauth2;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import com.verdict.verdict.entity.User;

import java.util.Collection;
import java.util.Map;

@Getter
@AllArgsConstructor
public class UserWithSignupStatus implements OAuth2User {
    private User user;
    private final OAuth2User delegate;
    private final String signupStatus; // "NEW" 또는 "EXISTING"

    @Override
    public Map<String, Object> getAttributes() {
        return delegate.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return delegate.getAuthorities();
    }

    @Override
    public String getName() {
        return user.getEmail();
    }
}