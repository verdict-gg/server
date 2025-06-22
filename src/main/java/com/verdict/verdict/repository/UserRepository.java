package com.verdict.verdict.repository;

import com.verdict.verdict.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
