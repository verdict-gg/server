package com.verdict.verdict.domain.comment.repository;

import com.verdict.verdict.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
