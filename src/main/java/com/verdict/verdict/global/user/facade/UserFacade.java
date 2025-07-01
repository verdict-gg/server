package com.verdict.verdict.global.user.facade;

import com.verdict.verdict.entity.User;
import com.verdict.verdict.global.user.dto.LoginRequest;
import com.verdict.verdict.global.user.dto.SignupRequest;
import com.verdict.verdict.global.dto.AuthResponse;
import com.verdict.verdict.global.dto.SignupResponse;

public interface UserFacade {
    void isNicknameDuplicate(String nickname);

    SignupResponse signup(SignupRequest signupRequest);

    AuthResponse getUserAuthInfo(String identifier);

    User getAuthUser(String identifier);

    User getGuestUser(LoginRequest loginRequest);
}