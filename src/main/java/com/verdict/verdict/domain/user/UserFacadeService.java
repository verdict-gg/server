package com.verdict.verdict.domain.user;

import com.verdict.verdict.domain.user.dto.AuthResponse;
import com.verdict.verdict.domain.user.dto.LoginRequest;
import com.verdict.verdict.domain.user.dto.SignupRequest;
import com.verdict.verdict.domain.user.entity.User;
import com.verdict.verdict.domain.user.service.UserService;
import com.verdict.verdict.global.entities.UserRole;
import com.verdict.verdict.global.exception.BusinessException;
import com.verdict.verdict.global.oAuth.dto.SignupResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static com.verdict.verdict.global.exception.ErrorCode.*;

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

        if (user.getUserRole() != UserRole.NOT_REGISTERED) {
            throw new BusinessException(ALREADY_REGISTERED);
        }

        user.updateUser(signupRequest.identifier(),signupRequest.nickname());
        updateRole(user);

        return SignupResponse.of(user.getId(), user.getIdentifier());
    }

    private void updateRole(User user) {
        if (adminIds.contains(user.getIdentifier())) {
            user.updateRole(UserRole.ADMIN);
            return;
        }
        user.updateRole(UserRole.USER);
    }

    @Override
    public AuthResponse getUserAuthInfo(String identifier) {
        User user = userService.findByIdentifier(identifier);
        return new AuthResponse(user.getUserRole());
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