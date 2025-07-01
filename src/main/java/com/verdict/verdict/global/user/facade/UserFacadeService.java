package com.verdict.verdict.global.user.facade;

import com.verdict.verdict.entity.Role;
import com.verdict.verdict.entity.User;
import com.verdict.verdict.global.user.dto.LoginRequest;
import com.verdict.verdict.global.user.dto.SignupRequest;
import com.verdict.verdict.global.user.service.UserService;
import com.verdict.verdict.global.dto.AuthResponse;
import com.verdict.verdict.global.dto.SignupResponse;
import com.verdict.verdict.global.util.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static com.verdict.verdict.global.util.ErrorCode.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserFacadeService implements UserFacade {
    private final UserService userService;


//    @Value("${guest.id}")
    private String guest_id;

//    @Value("${admin.githubId}")
    private List<String> adminIds;

    @Override
    public void isNicknameDuplicate(String nickname) {
        String target = nickname.trim();
        if (userService.findByNickname(target).isPresent()) {
            throw new BusinessException(DUPLICATED_NICKNAME);
        }
    }

    @Override
    @Transactional
    public SignupResponse signup(SignupRequest signupRequest) {
        User user = userService.findByIdentifier(signupRequest.identifier());

        if (user.getRole() != Role.NOT_REGISTERED) {
            throw new BusinessException(ALREADY_REGISTERED);
        }

        user.updateUser(signupRequest.identifier(),signupRequest.nickname());
        updateRole(user);

        return SignupResponse.of(user.getId(), user.getIdentifier());
    }

    private void updateRole(User user) {
        if (adminIds.contains(user.getIdentifier())) {
            user.updateRole(Role.ADMIN);
            return;
        }
        user.updateRole(Role.USER);
    }

    @Override
    public AuthResponse getUserAuthInfo(String identifier) {
        User user = userService.findByIdentifier(identifier);
        return new AuthResponse(user.getRole());
    }

    @Override
    public User getAuthUser(String identifier) {
        User user = userService.findByIdentifier(identifier);
        if (!user.isRegistered()) {
            throw new BusinessException(NOT_AUTHENTICATED_USER);
        }
        return user;
    }

    @Override
    public User getGuestUser(LoginRequest loginRequest) {
        if (!Objects.equals(loginRequest.id(), guest_id)) {
            throw new BusinessException(MEMBER_NOT_FOUND);
        }
        return userService.findByIdentifier(guest_id);
    }
}