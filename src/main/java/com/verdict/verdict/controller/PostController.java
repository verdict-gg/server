package com.verdict.verdict.controller;

import com.verdict.verdict.dto.ApiResponse;
import com.verdict.verdict.dto.response.PostExampleResponse;
import com.verdict.verdict.service.PostService;
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
    public ResponseEntity<ApiResponse<PostExampleResponse>> example() {
        PostExampleResponse response = new PostExampleResponse("text");
        return ResponseEntity.ok(ApiResponse.ok(response));
    }
}
