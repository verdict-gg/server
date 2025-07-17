package com.verdict.verdict.domain.post.controller;

import com.verdict.verdict.domain.post.dto.PostResponse;
import com.verdict.verdict.domain.post.service.PostService;
import com.verdict.verdict.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/posts")
@RestController
public class PostController {

    private final PostService postService;

    @GetMapping("/example")
    public ResponseEntity<ApiResponse<PostResponse>> example() {
        PostResponse response = new PostResponse("text");
        return ResponseEntity.ok(ApiResponse.ok(response));
    }
}
