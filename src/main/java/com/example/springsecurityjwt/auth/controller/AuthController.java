package com.example.springsecurityjwt.auth.controller;

import com.example.springsecurityjwt.auth.dto.TokenRequest;
import com.example.springsecurityjwt.auth.dto.TokenResponse;
import com.example.springsecurityjwt.auth.service.TokenService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final TokenService tokenService;

    @PostMapping("/token/refresh")
    public ResponseEntity<TokenResponse> reissueAccessToken(@RequestBody TokenRequest request) {
        TokenResponse response = tokenService.reissueAccessToken(request);
        return ResponseEntity.ok().body(response);
    }

}
