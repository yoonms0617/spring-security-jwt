package com.example.springsecurityjwt.auth.filter;

import com.example.springsecurityjwt.common.error.dto.ErrorResponse;
import com.example.springsecurityjwt.common.error.exception.ErrorType;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.AuthenticationException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class AuthenticationEntryPoint implements org.springframework.security.web.AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        log.info("AuthenticationEntryPoint RUN");
        String path = request.getRequestURI();
        String code = authException.getMessage();
        ErrorType errorType = ErrorType.findErrorTypeByCode(code);
        ErrorResponse errorResponse = ErrorResponse.of(errorType, path);
        objectMapper.writeValue(response.getWriter(), errorResponse);
    }

}
