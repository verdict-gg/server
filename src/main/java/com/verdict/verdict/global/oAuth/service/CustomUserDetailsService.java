package com.verdict.verdict.global.oAuth.service;

import com.verdict.verdict.domain.user.UserPrincipal;
import com.verdict.verdict.domain.user.UserRepository;
import com.verdict.verdict.domain.user.entity.User;
import com.verdict.verdict.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import static com.verdict.verdict.global.exception.ErrorCode.MEMBER_NOT_FOUND;


@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findById(Long.valueOf(username))
                .orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));

        return new UserPrincipal(user);
    }
}
