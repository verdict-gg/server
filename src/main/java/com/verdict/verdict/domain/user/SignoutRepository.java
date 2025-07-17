package com.verdict.verdict.domain.user;

import com.verdict.verdict.domain.user.entity.Signout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface SignoutRepository extends JpaRepository<Signout, Long> {
    @Query("select s from Signout s where s.identifier = :identifier")
    Signout findByIdentifier(String identifier);
}