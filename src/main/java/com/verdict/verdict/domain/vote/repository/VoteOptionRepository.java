package com.verdict.verdict.domain.vote.repository;

import com.verdict.verdict.domain.vote.entity.VoteOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteOptionRepository extends JpaRepository<VoteOption, Long> {
}
