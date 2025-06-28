package com.verdict.verdict.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CompletedPartDto {
    private Integer partNumber;

    @JsonProperty("eTag")
    private String eTag;
}
