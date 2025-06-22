package com.verdict.verdict.repository;

import com.verdict.verdict.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
