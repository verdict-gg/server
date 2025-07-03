package com.verdict.verdict.domain.attachment.image;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ImageUploadResult {
    private String imageUrl;
    private String fileKey;
}
