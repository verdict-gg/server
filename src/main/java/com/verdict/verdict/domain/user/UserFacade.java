package com.verdict.verdict.domain.user;

import com.verdict.verdict.domain.user.dto.AuthResponse;
import com.verdict.verdict.domain.user.dto.LoginRequest;
import com.verdict.verdict.domain.user.dto.SignupRequest;
import com.verdict.verdict.domain.user.entity.User;
import com.verdict.verdict.global.oAuth.dto.SignupResponse;


public interface UserFacade {
    void isNicknameDuplicate(String nickname);

    SignupResponse signup(SignupRequest signupRequest);

    AuthResponse getUserAuthInfo(String identifier);

    User getAuthUser(String identifier);

    User getGuestUser(LoginRequest loginRequest);
}