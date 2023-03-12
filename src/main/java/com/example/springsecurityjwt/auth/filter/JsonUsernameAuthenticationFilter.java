package com.example.springsecurityjwt.auth.filter;

import com.example.springsecurityjwt.auth.dto.LoginRequest;
import com.example.springsecurityjwt.common.error.exception.ErrorType;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.util.StreamUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import java.nio.charset.StandardCharsets;

@Slf4j
public class JsonUsernameAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final String DEFAULT_FILTER_PROCESSES_URL = "/api/member/login";

    private final ObjectMapper objectMapper;

    public JsonUsernameAuthenticationFilter(ObjectMapper objectMapper) {
        super(DEFAULT_FILTER_PROCESSES_URL);
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        log.info("JsonUsernameAuthenticationFilter RUN");
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException(ErrorType.METHOD_NOT_SUPPORTED.getCode());
        }
        LoginRequest loginRequest = parseLoginRequest(request);
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        UsernamePasswordAuthenticationToken authRequest =
                UsernamePasswordAuthenticationToken.unauthenticated(email, password);
        setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    protected void setDetails(HttpServletRequest request, UsernamePasswordAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }

    private LoginRequest parseLoginRequest(HttpServletRequest request) throws IOException {
        String jsonToString = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
        return objectMapper.readValue(jsonToString, LoginRequest.class);
    }

}
