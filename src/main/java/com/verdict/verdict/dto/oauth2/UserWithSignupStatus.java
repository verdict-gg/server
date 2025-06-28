package com.verdict.verdict.dto.oauth2;


import com.verdict.verdict.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Getter
public class UserWithSignupStatus implements OAuth2User {

    private User user;
    private boolean isNewUser;
    private Map<String, Object> attributes;


    public UserWithSignupStatus(User user, Map<String, Object> attributes) {
        this.user = user;
        this.isNewUser = user.getId() == null;
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // ROLE_USER | ROLE_ADMIN
        return user.getEmail().contains("ruukr41@naver.com") ?
                Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN"))
                : Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
    }
    @Override
    public String getName() {

        return this.getName();
    }
}