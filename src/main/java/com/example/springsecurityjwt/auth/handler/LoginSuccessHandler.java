package com.example.springsecurityjwt.auth.handler;

import com.example.springsecurityjwt.auth.domain.LoginMember;
import com.example.springsecurityjwt.auth.dto.LoginResponse;
import com.example.springsecurityjwt.auth.support.JwtProvider;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;

    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        LoginMember principal = (LoginMember) authentication.getPrincipal();
        String accessToken = jwtProvider.createAccessToken(principal.getId(), principal.getUsername(), principal.getRole());
        String refreshToken = jwtProvider.createRefreshToken(principal.getId(), principal.getUsername(), principal.getRole());
        objectMapper.writeValue(response.getWriter(), new LoginResponse(accessToken, refreshToken));
    }

}
