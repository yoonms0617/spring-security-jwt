package com.example.springsecurityjwt.auth.handler;

import com.example.springsecurityjwt.common.error.dto.ErrorCode;
import com.example.springsecurityjwt.common.error.dto.ErrorResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

public class LoginFailureHandler implements AuthenticationFailureHandler {

    private final ObjectMapper objectMapper;

    public LoginFailureHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        ErrorResponse errorResponse = null;
        if (exception instanceof BadCredentialsException) {
            errorResponse = ErrorResponse.of(ErrorCode.WRONG_EMAIL_PASSWORD);
        } else if (exception instanceof AuthenticationServiceException) {
            errorResponse = ErrorResponse.of(ErrorCode.METHOD_NOT_SUPPORTED);
        }
        objectMapper.writeValue(response.getWriter(), errorResponse);
    }

}
