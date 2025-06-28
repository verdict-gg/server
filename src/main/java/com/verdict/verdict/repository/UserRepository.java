package com.verdict.verdict.repository;

import com.verdict.verdict.dto.oauth2.ProviderInfo;
import com.verdict.verdict.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByIdentifier(String identifier);
    Optional<User> findByNickname(String nickname);
    List<User> findAllByIdentifier(String identifier);

    Optional<User> findByOAuth2Info(@Param("identifier") String identifier,
                                   @Param("providerInfo") ProviderInfo providerInfo);

}
