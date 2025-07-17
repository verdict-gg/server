package com.verdict.verdict.domain.attachment.video;

import com.verdict.verdict.domain.attachment.dto.CompletedPartDto;
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
