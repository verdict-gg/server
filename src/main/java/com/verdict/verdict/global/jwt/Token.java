package com.verdict.verdict.global.jwt;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Getter
//@Document(collection = "Token")
@NoArgsConstructor
public class Token {
    @Id
    private String identifier;

    private String token;

    @Builder
    public Token(String identifier, String token) {
        this.identifier = identifier;
        this.token = token;
    }
}