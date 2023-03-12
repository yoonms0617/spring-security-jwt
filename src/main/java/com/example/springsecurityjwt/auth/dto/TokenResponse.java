package com.example.springsecurityjwt.auth.dto;

import lombok.Getter;

@Getter
public class TokenResponse {

    private final String accesstoken;

    public TokenResponse(String accesstoken) {
        this.accesstoken = accesstoken;
    }

}
