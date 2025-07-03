package com.verdict.verdict.dto;

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
