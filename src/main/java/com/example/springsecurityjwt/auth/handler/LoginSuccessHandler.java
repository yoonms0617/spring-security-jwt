package com.example.springsecurityjwt.auth.handler;

import com.example.springsecurityjwt.auth.domain.AuthMember;
import com.example.springsecurityjwt.auth.dto.LoginResponse;
import com.example.springsecurityjwt.auth.service.JwtService;

import com.example.springsecurityjwt.auth.service.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;

    private final TokenService tokenService;

    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        log.info("LoginSuccessHandler RUN");
        log.info("Authentication = {}", authentication.getPrincipal());
        AuthMember principal = (AuthMember) authentication.getPrincipal();
        String email = principal.getEmail();
        String role = principal.getRole();
        LoginResponse loginResponse = jwtService.createTokenResponse(email, role);
        tokenService.syncRefreshToken(loginResponse.getRefreshToken(), email);
        objectMapper.writeValue(response.getWriter(), loginResponse);
    }

}
