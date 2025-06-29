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
public class UserPrincipal implements OAuth2User {

    private User user;
    private String nameAttributeKey; //sns nickname
    private Map<String, Object> attributes;
    private Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(User user) {
        this.user = user;
        this.authorities = Collections.singleton(new SimpleGrantedAuthority(user.getRole().getKey()));
    }
    public UserPrincipal(User user, Map<String, Object> attributes,String nameAttributeKey) {
        this.user = user;
        this.authorities = Collections.singleton(new SimpleGrantedAuthority(user.getRole().getKey()));
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
    }

    /**
     * OAuth2User 인터페이스에 있던 메서드. OAuth2UserSuccessHandler 에서 씀
     * @return (User) user.getIdentifier();
     */
    @Override
    public String getName() {
        return user.getIdentifier();
    }
}
