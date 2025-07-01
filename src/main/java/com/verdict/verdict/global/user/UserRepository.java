package com.verdict.verdict.global.user;

import com.verdict.verdict.global.constants.ProviderInfo;
import com.verdict.verdict.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByIdentifier(String identifier);
    Optional<User> findByNickname(String nickname);
    @Query("select u from User u where u.identifier = :identifier")
    List<User> findAllByIdentifier(String identifier);
    @Query("select u from User u where u.identifier = :identifier and u.providerInfo = :providerInfo")
    Optional<User> findByOAuthInfo(@Param("identifier") String identifier,
                                   @Param("providerInfo") ProviderInfo providerInfo);
}
