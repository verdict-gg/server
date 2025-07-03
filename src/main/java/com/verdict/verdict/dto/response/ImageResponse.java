package com.verdict.verdict.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ImageResponse {
    private String imageUrl;

    public static ImageResponse of(String imageUrl) {
        return new ImageResponse(imageUrl);
    }
}
