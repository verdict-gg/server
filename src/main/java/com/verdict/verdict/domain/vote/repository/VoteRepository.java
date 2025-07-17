package com.verdict.verdict.domain.vote.repository;

import com.verdict.verdict.domain.vote.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote, Long> {
}
