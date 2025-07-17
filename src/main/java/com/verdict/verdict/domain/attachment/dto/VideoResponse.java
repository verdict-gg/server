package com.verdict.verdict.domain.attachment.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class VideoResponse {
    private String videoUrl;

    public static VideoResponse of(String videoUrl) {
        return new VideoResponse(videoUrl);
    }
}
