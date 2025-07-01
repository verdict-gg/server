package com.verdict.verdict.global.user.service;


import com.verdict.verdict.entity.User;
import com.verdict.verdict.global.user.UserRepository;
import com.verdict.verdict.global.signOut.Signout;
import com.verdict.verdict.global.signOut.SignoutRepository;
import com.verdict.verdict.global.util.BusinessException;
import com.verdict.verdict.global.util.EncryptUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.verdict.verdict.global.util.ErrorCode.MEMBER_NOT_FOUND;


@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final SignoutRepository signoutRepository;
    private final EncryptUtil encryptUtil;



    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));
    }

    public User findByIdentifier(String identifier) {
        return userRepository.findByIdentifier(identifier)
                .orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));
    }

    public Optional<User> findByNickname(String nickname) {
        return userRepository.findByNickname(nickname);
    }

    @Transactional
    public Long save(User user) {
        return userRepository.saveAndFlush(user).getId();
    }


    @Transactional
    public void delete(Long userId, String identifier, String reason) {
        userRepository.deleteById(userId);
        signoutRepository.save(
                Signout.builder()
                        .identifier(identifier)
                        .reason(reason)
                        .build());
    }
}