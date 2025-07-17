package com.verdict.verdict.domain.post;

import com.verdict.verdict.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
