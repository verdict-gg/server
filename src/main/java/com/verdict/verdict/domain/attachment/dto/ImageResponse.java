package com.verdict.verdict.domain.attachment.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ImageResponse {
    private String imageUrl;


    public static ImageResponse of(String imageUrl) {
        return new ImageResponse(imageUrl);
    }
}
