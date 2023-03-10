package com.example.springsecurityjwt.auth.handler;

import com.example.springsecurityjwt.common.error.dto.ErrorResponse;
import com.example.springsecurityjwt.common.error.exception.ErrorType;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class LoginFailureHandler implements AuthenticationFailureHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String path = request.getRequestURI();
        String code = exception.getMessage();
        ErrorType errorType = ErrorType.findErrorTypeByCode(code);
        ErrorResponse errorResponse = ErrorResponse.of(errorType, path);
        response.setStatus(errorResponse.getStatus());
        objectMapper.writeValue(response.getWriter(), errorResponse);
    }

}
