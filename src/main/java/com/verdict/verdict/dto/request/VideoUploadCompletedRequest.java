package com.verdict.verdict.dto.request;

import com.verdict.verdict.dto.CompletedPartDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class VideoUploadCompletedRequest {
    private String uploadId;
    private String fileKey;
    private List<CompletedPartDto> completedParts;
}
